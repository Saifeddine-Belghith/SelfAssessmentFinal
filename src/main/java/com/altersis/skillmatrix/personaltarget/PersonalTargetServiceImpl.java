package com.altersis.skillmatrix.personaltarget;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.employee.EmployeeRepository;
import com.altersis.skillmatrix.enumeration.SupportedValue;
import com.altersis.skillmatrix.enumeration.TargetArea;
import com.altersis.skillmatrix.enumeration.TargetStatus;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.altersis.skillmatrix.ClientFeedback.ClientFeedbackRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@NoArgsConstructor
public class PersonalTargetServiceImpl implements PersonalTargetService {

    @Autowired
    private PersonalTargetRepository personalTargetRepository;
    @Autowired
    private ClientFeedbackRepository clientFeedbackRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public PersonalTargetServiceImpl(PersonalTargetRepository personalTargetRepository,
                                     EmployeeRepository employeeRepository,
                                     ClientFeedbackRepository clientFeedbackRepository) {
        this.personalTargetRepository = personalTargetRepository;
        this.employeeRepository = employeeRepository;
        this.clientFeedbackRepository = clientFeedbackRepository;
    }


    @Override
    public PersonalTargetDTO createPersonalTarget(PersonalTargetDTO personalTargetDTO, Long idEmployee) {
        Employee employee = employeeRepository.getById(idEmployee);
        if (employee == null) {
            throw new IllegalArgumentException("Employee with ID " + idEmployee + " not found.");
        }

        PersonalTarget personalTarget = new PersonalTarget();
        personalTarget.setSkill(personalTargetDTO.getSkill());
        personalTarget.setDescription(personalTargetDTO.getDescription());
        personalTarget.setAcceptanceCriteria(personalTargetDTO.getAcceptanceCriteria());
        // Convert the targetArea string to the corresponding TargetArea enum
        TargetArea targetArea = TargetArea.valueOf(personalTargetDTO.getTargetArea());
        personalTarget.setTargetArea(targetArea);
        personalTarget.setStatus(TargetStatus.IN_PROGRESS);
        personalTarget.setEmployee(employee);
        personalTarget.setTargetDate(personalTargetDTO.getTargetDate());
        personalTarget.setQuarter(personalTargetDTO.getQuarter());
        // Convert the supportedValue string to the corresponding SupportedValue enum
        SupportedValue supportedValue = SupportedValue.valueOf(personalTargetDTO.getSupportedValue());
        personalTarget.setSupportedValue(supportedValue);
        personalTarget.setOrigin(employee);
        personalTarget.setDefinedBy("Defined by Consultant");

        PersonalTarget savedPersonalTarget = personalTargetRepository.save(personalTarget);

        return new PersonalTargetDTO(savedPersonalTarget);
    }


    //For Manager and Coach
    @Override
    public PersonalTargetDTO assignPersonalTarget(PersonalTargetDTO personalTargetDTO, Long idEmployee, Long originId) {
        Employee employee = employeeRepository.getById(idEmployee);
        if (employee == null) {
            throw new IllegalArgumentException("Employee with ID " + idEmployee + " not found.");
        }

        Employee origin = employeeRepository.getById(originId);
        if (origin == null) {
            throw new IllegalArgumentException("Origin employee with ID " + originId + " not found.");
        }

        // Perform additional validation to ensure the origin is valid for the employee (coach or manager)

        PersonalTarget personalTarget = new PersonalTarget();
        personalTarget.setSkill(personalTargetDTO.getSkill());
        personalTarget.setDescription(personalTargetDTO.getDescription());
        personalTarget.setAcceptanceCriteria(personalTargetDTO.getAcceptanceCriteria());
        // Convert the targetArea string to the corresponding TargetArea enum
        TargetArea targetArea = TargetArea.valueOf(personalTargetDTO.getTargetArea());
        personalTarget.setTargetArea(targetArea);
        personalTarget.setStatus(TargetStatus.IN_PROGRESS);
        personalTarget.setEmployee(employee);
        personalTarget.setTargetDate(personalTargetDTO.getTargetDate());
        personalTarget.setQuarter(personalTargetDTO.getQuarter());
        // Convert the supportedValue string to the corresponding SupportedValue enum
        SupportedValue supportedValue = SupportedValue.valueOf(personalTargetDTO.getSupportedValue());
        personalTarget.setSupportedValue(supportedValue);
        personalTarget.setOrigin(origin);

        // Set the 'definedBy' field based on the conditions
        if (origin.getIsCoach() && !origin.getIsManager()) {
            personalTarget.setDefinedBy("Defined by Coach");
        } else if (origin.getIsManager() && !origin.getIsCoach()) {
            personalTarget.setDefinedBy("Defined by Manager");
        } else if (origin.getIsManager() && origin.getIsCoach()) {
            personalTarget.setDefinedBy("Defined by Manager and Coach");
        }

        PersonalTarget savedPersonalTarget = personalTargetRepository.save(personalTarget);

        return new PersonalTargetDTO(savedPersonalTarget);
    }



    @Override
    public PersonalTargetDTO updatePersonalTargetStatus(Long personalTargetId, TargetStatus status) throws ResourceNotFoundException {
        PersonalTarget personalTarget = personalTargetRepository.findById(personalTargetId)
                .orElseThrow(() -> new ResourceNotFoundException("Personal ClientFeedback not found with id " + personalTargetId));

        personalTarget.setStatus(status);
        personalTarget = personalTargetRepository.save(personalTarget);

        return new PersonalTargetDTO(personalTarget);
    }

    @Override
    public void deletePersonalTarget(Long personalTargetId) {
        personalTargetRepository.deleteById(personalTargetId);
    }

    @Override
    public List<PersonalTargetDTO> getPersonalTargetsByEmployee(Long idEmployee) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(idEmployee)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + idEmployee));

        List<PersonalTarget> personalTargets = personalTargetRepository.findAllByEmployeeIdEmployee(idEmployee);
        List<PersonalTargetDTO> personalTargetDTOs = new ArrayList<>();

        for (PersonalTarget personalTarget : personalTargets) {
            personalTargetDTOs.add(new PersonalTargetDTO(personalTarget));
        }

        return personalTargetDTOs;
    }

    @Override
    public List<EmployeeDTO> searchConsultantsByTarget(String skill, /*List<TargetArea> targetAreas,*/ List<TargetStatus> targetStatuses, /*List<SupportedValue> supportedValues,*/ int targetDate,List<String> quarters) {
        if (skill == null) {
            skill = "";
        }
//        if (targetAreas == null) {
//            targetAreas = Collections.emptyList();
//        }
        if (targetStatuses == null) {
            targetStatuses = Collections.emptyList();
        }
//        if (supportedValues == null) {
//            supportedValues = Collections.emptyList();
//        }
        if (targetDate == 0) {
            targetDate = 0;
        }
        if (quarters == null) {
            quarters = Collections.emptyList();
        }

        List<PersonalTarget> personalTargets;

        if (skill=="" /*&& targetAreas.isEmpty()*/ && targetStatuses.isEmpty() && /*supportedValues.isEmpty() &&*/ targetDate == 0 && quarters.isEmpty()){
            personalTargets = personalTargetRepository.findAll();
        } else {
            personalTargets = personalTargetRepository.findBySkillAndStatusInAndTargetDateAndQuarterIn(skill, /*targetAreas,*/ targetStatuses, /*supportedValues,*/ targetDate,quarters);
        }

        List<EmployeeDTO> consultants = new ArrayList<>();

        for (PersonalTarget personalTarget : personalTargets) {
            consultants.add(new EmployeeDTO(personalTarget.getEmployee()));
        }

        return consultants;
    }
}

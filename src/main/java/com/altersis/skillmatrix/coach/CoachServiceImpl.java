package com.altersis.skillmatrix.coach;

import com.altersis.skillmatrix.coachee.Coachee;
import com.altersis.skillmatrix.coachee.CoacheeDTO;
import com.altersis.skillmatrix.coachee.CoacheeRepository;
import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.employee.EmployeeRepository;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private CoacheeRepository coacheeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Service
    public class CoachService {
        @Autowired
        private CoachRepository coachRepository;

        public CoachDTO save(CoachDTO coachDTO) {
            Coach coach = new Coach();
            coach.setIdEmployee(coachDTO.getIdEmployee());
            coach.setFirstName(coachDTO.getFirstName());
            coach.setLastName(coachDTO.getLastName());
            coach.setEmail(coachDTO.getEmail());
            coach.setPassword(coachDTO.getPassword());
            coach.setRole(coachDTO.getRole());
            coach.setPhone(coachDTO.getPhone());
            coach.setExperienceLevel(coachDTO.getExperienceLevel());

            Coach savedCoach = coachRepository.save(coach);

            List<EmployeeDTO> coacheeDTOs = coachDTO.getCoachees();
            List<Coachee> coachees = new ArrayList<>();
            for (EmployeeDTO coacheeDTO : coacheeDTOs) {
                Coachee coachee = new Coachee();
                coachee.setIdEmployee(coacheeDTO.getIdEmployee());
                coachee.setFirstName(coacheeDTO.getFirstName());
                coachee.setLastName(coacheeDTO.getLastName());
                coachee.setEmail(coacheeDTO.getEmail());
                coachee.setPassword(coacheeDTO.getPassword());
                coachee.setRole(coacheeDTO.getRole());
                coachee.setPhone(coacheeDTO.getPhone());
                coachee.setExperienceLevel(coacheeDTO.getExperienceLevel());
                coachee.setCoach(savedCoach);

                coachees.add(coachee);
            }

            List<Coachee> savedCoachees = coacheeRepository.saveAll(coachees);

            List<EmployeeDTO> savedCoacheeDTOs = new ArrayList<>();
            for (Coachee savedCoachee : savedCoachees) {
                CoacheeDTO savedCoacheeDTO = new CoacheeDTO();
                savedCoacheeDTO.setIdCoachee(savedCoachee.getIdCoachee());
                savedCoacheeDTO.setIdEmployee(savedCoachee.getIdEmployee());
                savedCoacheeDTO.setFirstName(savedCoachee.getFirstName());
                savedCoacheeDTO.setLastName(savedCoachee.getLastName());
                savedCoacheeDTO.setEmail(savedCoachee.getEmail());
                savedCoacheeDTO.setPassword(savedCoachee.getPassword());
                savedCoacheeDTO.setRole(savedCoachee.getRole());
                savedCoacheeDTO.setPhone(savedCoachee.getPhone());
                savedCoacheeDTO.setExperienceLevel(savedCoachee.getExperienceLevel());
                savedCoacheeDTO.setCoach(coachDTO);

                savedCoacheeDTOs.add(savedCoacheeDTO);
            }

            coachDTO.setIdCoach(savedCoach.getIdCoach());
            coachDTO.setCoachees(savedCoacheeDTOs);

            return coachDTO;
        }
    }

    @Override
    public void assignCoach(Long coacheeId, Long coachId) {
        Employee coachee = employeeRepository.findById(coacheeId)
                .orElseThrow(() -> new ResourceNotFoundException("Coachee not found with id " + coacheeId));

        Employee coach = employeeRepository.findById(coachId)
                .orElseThrow(() -> new ResourceNotFoundException("Coach not found with id " + coachId));

        coachee.setCoach(coach);

        employeeRepository.save(coachee);

    }

    @Override
    public CoachDTO save(CoachDTO coachDTO) {
        Coach coach = new Coach();
        coach.setFirstName(coachDTO.getFirstName());
        coach.setLastName(coachDTO.getLastName());
        coach.setEmail(coachDTO.getEmail());
        coach.setPassword(coachDTO.getPassword());
        coach.setRole(coachDTO.getRole());
        coach.setPhone(coachDTO.getPhone());
        coach.setExperienceLevel(coachDTO.getExperienceLevel());

        Coach savedCoach = coachRepository.save(coach);

        List<EmployeeDTO> coacheeDTOs = coachDTO.getCoachees();
        List<Coachee> coachees = new ArrayList<>();
        for (EmployeeDTO coacheeDTO : coacheeDTOs) {
            Coachee coachee = new Coachee();
            coachee.setIdEmployee(coacheeDTO.getIdEmployee());
            coachee.setFirstName(coacheeDTO.getFirstName());
            coachee.setLastName(coacheeDTO.getLastName());
            coachee.setEmail(coacheeDTO.getEmail());
            coachee.setPassword(coacheeDTO.getPassword());
            coachee.setRole(coacheeDTO.getRole());
            coachee.setPhone(coacheeDTO.getPhone());
            coachee.setExperienceLevel(coacheeDTO.getExperienceLevel());
            coachee.setCoach(savedCoach);

            coachees.add(coachee);
        }

        List<Coachee> savedCoachees = coacheeRepository.saveAll(coachees);

        List<EmployeeDTO> savedCoacheeDTOs = new ArrayList<>();
        for (Coachee savedCoachee : savedCoachees) {
            EmployeeDTO savedCoacheeDTO = new CoacheeDTO();
            savedCoacheeDTO.setIdEmployee(savedCoachee.getIdCoachee());
            savedCoacheeDTO.setIdEmployee(savedCoachee.getIdEmployee());
            savedCoacheeDTO.setFirstName(savedCoachee.getFirstName());
            savedCoacheeDTO.setLastName(savedCoachee.getLastName());
            savedCoacheeDTO.setEmail(savedCoachee.getEmail());
            savedCoacheeDTO.setPassword(savedCoachee.getPassword());
            savedCoacheeDTO.setRole(savedCoachee.getRole());
            savedCoacheeDTO.setPhone(savedCoachee.getPhone());
            savedCoacheeDTO.setExperienceLevel(savedCoachee.getExperienceLevel());
            savedCoacheeDTO.setCoach(coachDTO);

            savedCoacheeDTOs.add(savedCoacheeDTO);
        }

        coachDTO.setIdCoach(savedCoach.getIdCoach());
        coachDTO.setCoachees(savedCoacheeDTOs);

        return coachDTO;
    }



//    @Override
//    public List<CoachDTO> findAll() {
//        List<Coach> coaches = coachRepository.findAll();
//        return coaches.stream()
//                .map(coach -> new CoachDTO(
//                        coach.getIdCoach(),
//                        coach.getIdEmployee(),
//                        coach.getFirstName(),
//                        coach.getLastName(),
//                        coach.getEmail(),
//                        coach.getPassword(),
//                        coach.getRole(),
//                        coach.getPhone(),
//                        coach.getExperienceLevel(),
//                        coach.getCoachees().stream()
//                                .map(coachee -> new CoacheeDTO(
//                                        coachee.getIdCoachee(),
//                                        coachee.getIdEmployee(),
//                                        coachee.getFirstName(),
//                                        coachee.getLastName(),
//                                        coachee.getEmail(),
//                                        coachee.getPassword(),
//                                        coachee.getPhone(),
//                                        coachee.getRole(),
//                                        coachee.getExperienceLevel(),
//                                        new CoachDTO(
//                                                coachee.getCoach().getIdCoach(),
//                                                coachee.getCoach().getIdEmployee(),
//                                                coachee.getCoach().getFirstName(),
//                                                coachee.getCoach().getLastName(),
//                                                coachee.getCoach().getEmail(),
//                                                coachee.getCoach().getPassword(),
//                                                coachee.getCoach().getPhone(),
//                                                coachee.getCoach().getRole(),
//                                                coachee.getCoach().getExperienceLevel(),
//                                                null
//                                        )
//                                ))
//                                .collect(Collectors.toList())
//                ))
//                .collect(Collectors.toList());
//    }

    @Override
    public CoachDTO findById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<EmployeeDTO> getCoacheesByCoachId(Long coachId) {
        return employeeRepository.findByIdEmployee(coachId);
    }
@Override
    public CoachDTO getCoachByCoachId(Long coachId) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("Coach not found")  );
        return new CoachDTO(
                coach.getIdCoach(),
                coach.getIdEmployee(),
                coach.getFirstName(),
                coach.getLastName(),
                coach.getEmail(),
                coach.getPassword(),

                coach.getPhone(),
                coach.getExperienceLevel(),
                coach.getRole(),
                null
        );
    }


    @Override
    public void assignCoacheeToCoach(Long coacheeId, Long coachId) {

    }
}
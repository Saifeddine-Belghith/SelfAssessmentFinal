package com.altersis.skillmatrix.employee;

import com.altersis.skillmatrix.assessment.Assessment;
import com.altersis.skillmatrix.assessment.AssessmentRepository;
import com.altersis.skillmatrix.coach.Coach;
import com.altersis.skillmatrix.coach.CoachRepository;
import com.altersis.skillmatrix.coachee.Coachee;
import com.altersis.skillmatrix.coachee.CoacheeRepository;
import com.altersis.skillmatrix.enumeration.Experience;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.altersis.skillmatrix.manager.Manager;
import com.altersis.skillmatrix.manager.ManagerRepository;
import com.altersis.skillmatrix.skill.Skill;
import com.altersis.skillmatrix.skill.SkillRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private CoacheeRepository coacheeRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @PersistenceContext
    private EntityManager entityManager;

    //Get all Employee
    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/getOtherEmployees/{idEmployee}")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(@PathVariable Long idEmployee) {
        List<EmployeeDTO> employees = employeeService.findAllEmployees();
        // Filter out the employee record based on the provided employee ID
        employees = employees.stream()
                .filter(employee -> employee.getIdEmployee() != idEmployee)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }





    //Get Employee by id
    @GetMapping("/{idEmployee}")
    public EmployeeDTO getEmployeeById(@PathVariable Long idEmployee) {
        Employee employee = employeeRepository.findById(idEmployee)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + idEmployee));

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFromEntity(employee);

        if (employee.getIsCoach()) {
            List<EmployeeDTO> coachees = new ArrayList<>();
            for (Employee coachee : employee.getCoachees()) {
                EmployeeDTO coacheeDTO = new EmployeeDTO();
                coacheeDTO.setFromEntity(coachee);
                coachees.add(coacheeDTO);
            }
            employeeDTO.setCoachees(coachees);
        }

        if (employee.getCoach() != null) {
            EmployeeDTO coachDTO = new EmployeeDTO();
            coachDTO.setFromEntity(employee.getCoach());
            employeeDTO.setCoach(coachDTO);
        }

        if (employee.getManager() != null) {
            EmployeeDTO managerDTO = new EmployeeDTO();
            managerDTO.setFromEntity(employee.getManager());
            employeeDTO.setManager(managerDTO);
        }

        return employeeDTO;
    }




    //Create a new employee
    @JsonIgnoreProperties(value = {"assessments", "setCoach"}, allowSetters = true)
    @PostMapping("/AddEmployee")
    public EmployeeDTO save(@RequestBody EmployeeDTO employeeDto) {
        EmployeeDTO createdEmployeeDTO = employeeService.createEmployee(employeeDto);
//        if (Boolean.TRUE.equals(employeeDto.getIsCoach())) {
//            Coach coach = new Coach();
//            copyEmployeeDtoPropertiesToEmployee(coach, createdEmployeeDTO);
//            coach.setIsCoach(true);
//            coachRepository.save(coach);
//            updateEmployeeIsCoachFlag(createdEmployeeDTO.getIdEmployee(), true);
//        }
//        if (Boolean.TRUE.equals(employeeDto.getIsCoachee())) {
//            Coachee coachee = new Coachee();
//            copyEmployeeDtoPropertiesToEmployee(coachee, createdEmployeeDTO);
//            coachee.setIsCoachee(true);
//            coacheeRepository.save(coachee);
//            updateEmployeeIsCoacheeFlag(createdEmployeeDTO.getIdEmployee(), true);
//        }
//         if (Boolean.TRUE.equals(employeeDto.getIsManager()) ){
//            Manager manager = new Manager();
//            copyEmployeeDtoPropertiesToEmployee(manager, createdEmployeeDTO);
//            manager.setIsManager(true);
//             managerRepository.save(manager);
//            updateEmployeeIsManagerFlag(createdEmployeeDTO.getIdEmployee(), true);
//            updateEmployeeIsCoachFlag(createdEmployeeDTO.getIdEmployee(), createdEmployeeDTO.getIsCoach());
//
//
//         }
        return createdEmployeeDTO;
    }

    private void copyEmployeeDtoPropertiesToEmployee(Employee employee, EmployeeDTO employeeDto) {
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setPassword(employeeDto.getPassword());
        employee.setPhone(employeeDto.getPhone());
        employee.setExperienceLevel(employeeDto.getExperienceLevel());
        employee.setRole(employeeDto.getRole());
        employee.setIsCoachee(employeeDto.getIsCoachee());
        employee.setIsCoach(employeeDto.getIsCoach());
        employee.setIsManager(employeeDto.getIsManager());
        if (Boolean.TRUE.equals(employeeDto.getIsCoach())) {
            Coach coach = (Coach) employee;
            coach.setIsCoach(true);
        }
        if (Boolean.TRUE.equals(employeeDto.getIsCoachee())) {
            Coachee coachee = (Coachee) employee;
            coachee.setIsCoachee(true);
        }
        if (Boolean.TRUE.equals(employeeDto.getIsManager())) {
            Manager manager = (Manager) employee;
            manager.setIsManager(true);
        }
    }

    private void updateEmployeeIsCoachFlag(Long employeeId, boolean value) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        employee.setIsCoach(value);
        employee.setIsManager(employee.getIsManager());
        employeeRepository.save(employee);
    }

    private void updateEmployeeIsCoacheeFlag(Long employeeId, boolean value) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        employee.setIsCoachee(value);
        employeeRepository.save(employee);
    }

    private void updateEmployeeIsManagerFlag(Long employeeId, boolean value) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        employee.setIsManager(value);
        employeeRepository.save(employee);
    }

    //update employee
    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO updatedEmployee) {
            EmployeeDTO updateEmployee = employeeService.updateEmployee(id, updatedEmployee);
            return new ResponseEntity<>(updateEmployee, HttpStatus.ACCEPTED);
        }






    //Delete employee
//    @DeleteMapping("/delete/{id}")
//    public void deleteEmployee(@PathVariable Long id) {
//        employeeService.deleteById(id);
//    }

    @GetMapping("/{idEmployee}/coachees")
    public List<EmployeeDTO> getCoacheesByCoachId(@PathVariable Long idEmployee) {
        Employee coach = employeeRepository.findById(idEmployee)
                .orElseThrow(() -> new ResourceNotFoundException("Coach not found with id " + idEmployee));
        if (!coach.getIsCoach()) {
            throw new IllegalArgumentException("Employee with id " + idEmployee + " is not a coach");
        }
        List<EmployeeDTO> coachees = new ArrayList<>();

        for (Employee coachee : coach.getCoachees()) {
            EmployeeDTO coacheeDTO = new EmployeeDTO();
            coacheeDTO.setFromEntity(coachee);
            coachees.add(coacheeDTO);
        }
        return coachees;
    }

    @PostMapping("/assign-coach")
    public ResponseEntity<?> assignCoach(@RequestParam Long coacheeId, @RequestParam Long coachId) {
        employeeService.assignCoach(coacheeId, coachId);
        return ResponseEntity.ok().build();
    }
    private EmployeeDTO convertToDto(Employee employee) {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setIdEmployee(employee.getIdEmployee());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPassword(employee.getPassword());
        employeeDto.setPhone(employee.getPhone());
        employeeDto.setExperienceLevel(employee.getExperienceLevel());
    //    employeeDto.setPicture(employee.getPicture().getBytes());
        employeeDto.setRole(employee.getRole());
        return employeeDto;
    }
//find employees by experience Level
    @GetMapping("/employees/experience/{experienceLevel}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByExperience(@PathVariable Experience experienceLevel) {
        List<EmployeeDTO> employeeDTOs = employeeService.findEmployeesByExperience(experienceLevel);
        if (employeeDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(employeeDTOs);
        }
    }
    @GetMapping("/myProfile")
    public ResponseEntity<Employee> getMyProfile(@RequestParam(value = "email") String email) {
        Employee employee = employeeService.getMyProfile(email);
        return ResponseEntity.ok().body(employee);
    }

    @GetMapping("/search")
    public List<EmployeeDTO> searchConsultantsBySkillsAndRatings(
            @RequestParam(value="skills" , required = true) List<String> skills,
            @RequestParam("ratings") List<Integer> ratings) {
        if (skills.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if no skills are provided
        }
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> consultants = new ArrayList<>();

        for (Employee employee : employees) {
            boolean hasSkillsAndRating = true;

            for (int i = 0; i < skills.size(); i++) {
                String skillName = skills.get(i);
                List<Skill> matchingSkills = skillRepository.findSkillBySkillName(skillName);
                if (!matchingSkills.isEmpty()) {
                    Skill skill = matchingSkills.get(matchingSkills.size() - 1); // Get the last matching skill
                    List<Assessment> assessments = assessmentRepository.findAllByEmployeeAndSkillOrderByDateDesc(employee, skill);
                    if (!assessments.isEmpty()) {
                        Assessment lastAssessment = assessments.get(0); // Get the latest assessment
                        if (ratings.size() > i && !ratings.get(i).equals(lastAssessment.getRating())) {
                            hasSkillsAndRating = false;
                            break;
                        }
                    } else {
                        hasSkillsAndRating = false;
                        break;
                    }
                } else {
                    hasSkillsAndRating = false;
                    break;
                }
            }

            if (hasSkillsAndRating) {
                consultants.add(new EmployeeDTO(employee));
            }
        }

        return consultants;
    }



}

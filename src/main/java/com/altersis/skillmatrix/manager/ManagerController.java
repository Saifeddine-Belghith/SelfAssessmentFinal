package com.altersis.skillmatrix.manager;

import com.altersis.skillmatrix.assessment.Assessment;
import com.altersis.skillmatrix.assessment.AssessmentDTO;
import com.altersis.skillmatrix.assessment.AssessmentService;
import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.employee.EmployeeRepository;
import com.altersis.skillmatrix.employee.EmployeeServiceImpl;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.altersis.skillmatrix.skill.Skill;
import com.altersis.skillmatrix.skill.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    @Autowired
    private ManagerServiceImpl managerService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    AssessmentService assessmentService;
    @Autowired
    SkillRepository skillRepository;



    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        List<ManagerDTO> managers = managerService.findAll();
        if (managers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(managers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getManagerById(@PathVariable Long id) {
        ManagerDTO manager = managerService.findById(id);
        if (manager == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(manager, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ManagerDTO> createManager(@RequestBody ManagerDTO managerDTO) {
        ManagerDTO createdManager = managerService.save(managerDTO);
        if (createdManager == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdManager, HttpStatus.CREATED);
    }
    @PostMapping("/assign-manager")
    public ResponseEntity<?> assignManager(@RequestParam Long employeeId, @RequestParam Long managerId) {
        Optional<Employee> manager = employeeRepository.findById(managerId);
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (manager.get().getIsManager()==false ) {
            throw new ResourceNotFoundException("Manager not found with id " + managerId);
        } else if (employee.get().getIsManager()==false) {
        managerService.assignManager(employeeId, managerId);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idEmployee}/employees-managed")
    public List<EmployeeDTO> getEmployeesManagedByManagerId(@PathVariable Long idEmployee) {
        Employee manager = employeeRepository.findById(idEmployee)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id " + idEmployee));
        if (!manager.getIsManager()) {
            throw new IllegalArgumentException("Employee with id " + idEmployee + " is not a manager");
        }
        List<EmployeeDTO> employeesManaged = new ArrayList<>();
        for (Employee employee : manager.getEmployeesManaged()) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setFromEntity(employee);
            employeesManaged.add(employeeDTO);
        }
        return employeesManaged;
    }

    //average of skill of employees managed by manager
    @GetMapping("/{idEmployee}/team-average-for-skill/{idSkill}")
    public Double getTeamAverageForSkill(@PathVariable Long idEmployee, @PathVariable Long idSkill) {
        Employee manager = employeeRepository.findById(idEmployee)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id " + idEmployee));
        if (!manager.getIsManager()) {
            throw new IllegalArgumentException("Employee with id " + idEmployee + " is not a manager");
        }
        List<Employee> employeesManaged = manager.getEmployeesManaged();
        List<Assessment> assessments = new ArrayList<>();
        for (Employee employee : employeesManaged) {
            assessments.addAll(assessmentService.findByEmployeeIdEmployeeAndSkillIdSkill(employee.getIdEmployee(), idSkill));
        }
        Double average = assessments.stream()
                .mapToInt(Assessment::getRating)
                .average()
                .orElse(Double.NaN);
        return average;
    }

    //average of all skills on my team
    @GetMapping("/{idEmployee}/team-average-for-skills")
    public List<Map<String, Object>> getTeamAverageForSkills(@PathVariable Long idEmployee) {
        Employee manager = employeeRepository.findById(idEmployee)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id " + idEmployee));
        if (!manager.getIsManager()) {
            throw new IllegalArgumentException("Employee with id " + idEmployee + " is not a manager");
        }
        List<Employee> employeesManaged = manager.getEmployeesManaged();
        List<AssessmentDTO> assessments = new ArrayList<>();
        for (Employee employee : employeesManaged) {
            assessments.addAll(assessmentService.getLastAssessmentsByEmployee(employee.getIdEmployee()).stream()
                    .collect(Collectors.toList()));
        }
        Map<Long, List<Integer>> skillRatingsMap = new HashMap<>();
        for (AssessmentDTO assessment : assessments) {
            Long skillId = assessment.getIdSkill();
            Integer rating = assessment.getRating();
            List<Integer> ratings = skillRatingsMap.getOrDefault(skillId, new ArrayList<>());
            ratings.add(rating);
            skillRatingsMap.put(skillId, ratings);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, List<Integer>> entry : skillRatingsMap.entrySet()) {
            Long skillId = entry.getKey();
            List<Integer> ratings = entry.getValue();
            // Calculate the sum of ratings for the skill
            int sumRatings = ratings.stream().mapToInt(Integer::intValue).sum();

            // Calculate the average rating for the skill
            double average;
            if (ratings.isEmpty()) {
                // Handle the case when no employees have submitted assessments for this skill
                average = 0.0;
            } else {
                average = (double) sumRatings / employeesManaged.size();
            }
//            Double average = ratings.stream().mapToInt(Integer::intValue).average().orElse(Double.NaN);
            Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found with id " + skillId));
            Map<String, Object> skillResult = new HashMap<>();
            skillResult.put("skillName", skill.getSkillName());
            skillResult.put("averageRating", average);
            result.add(skillResult);
        }
        return result;
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ManagerDTO> updateManager(@PathVariable Long id, @RequestBody ManagerDTO managerDTO) {
        ManagerDTO updatedManager = managerService.update(id, managerDTO);
        if (updatedManager == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedManager, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteManager(@PathVariable Long id) {
        boolean result = managerService.deleteById(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.altersis.skillmatrix.coach;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.employee.EmployeeRepository;
import com.altersis.skillmatrix.employee.EmployeeService;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coach")
public class CoachController {

    @Autowired
    private CoachService coachService;
    @Autowired
    private  CoachRepository coachRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/create")
    public String createCoach(@RequestBody Coach coach) {
        Employee employee = new Employee();
        employee.setFirstName(coach.getFirstName());
        employee.setLastName(coach.getLastName());
        employee.setEmail(coach.getEmail());
        employee.setPassword(coach.getPassword());
        employee.setRole(coach.getRole());
        employee.setPhone(coach.getPhone());
        employee.setExperienceLevel(coach.getExperienceLevel());



        coachRepository.save(coach);

        return "New coach created with ID: " + coach.getIdCoach();
    }
//    @GetMapping("/{coachId}/coachees")
//    public ResponseEntity<List<EmployeeDTO>> getCoacheesByCoachId(@PathVariable Long coachId) {
//
//        List<EmployeeDTO> coachees = coachService.getCoacheesByCoachId(coachId);
//        return ResponseEntity.ok(coachees);
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
        coachService.assignCoach(coacheeId, coachId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{coachId}/my-coach")
    public ResponseEntity<CoachDTO> getCoachByCoachId(@PathVariable Long coachId) {
        CoachDTO coach = coachService.getCoachByCoachId(coachId);
        return ResponseEntity.ok(coach);
    }
}

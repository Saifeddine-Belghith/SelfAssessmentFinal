package com.altersis.skillmatrix.personaltarget;


import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.enumeration.SupportedValue;
import com.altersis.skillmatrix.enumeration.TargetArea;
import com.altersis.skillmatrix.enumeration.TargetStatus;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personal-targets")
public class PersonalTargetController {

    private final PersonalTargetService personalTargetService;

    public PersonalTargetController(PersonalTargetService personalTargetService) {
        this.personalTargetService = personalTargetService;
    }

    //create new personal Target
    @PostMapping("/create/{idEmployee}")
    public ResponseEntity<PersonalTargetDTO> createPersonalTarget(@PathVariable Long idEmployee,
                                                                  @RequestBody PersonalTargetDTO personalTargetDTO) {
        PersonalTargetDTO createdPersonalTarget = personalTargetService.createPersonalTarget(personalTargetDTO, idEmployee);
        return ResponseEntity.ok(createdPersonalTarget);
    }

    //assign Personal Target to employee
    @PostMapping("/assign/{idEmployee}/origins/{originId}")
    public ResponseEntity<PersonalTargetDTO> assignPersonalTarget(@RequestBody PersonalTargetDTO personalTargetDTO, @PathVariable Long idEmployee, @PathVariable Long originId) {
        PersonalTargetDTO createdPersonalTarget = personalTargetService.assignPersonalTarget(personalTargetDTO, idEmployee, originId);
        return ResponseEntity.ok(createdPersonalTarget);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PersonalTargetDTO> updatePersonalTargetStatus(@PathVariable("id") Long idPersonalTarget, @RequestBody PersonalTarget request) throws ResourceNotFoundException {
        PersonalTargetDTO personalTargetDTO = personalTargetService.updatePersonalTargetStatus(idPersonalTarget, request.getStatus() );
        return ResponseEntity.ok(personalTargetDTO);
    }

    @DeleteMapping("/{idPersonalTarget}")
    public ResponseEntity<Void> deletePersonalTarget(@PathVariable Long idPersonalTarget) {
        personalTargetService.deletePersonalTarget(idPersonalTarget);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{idEmployee}")
    public ResponseEntity<List<PersonalTargetDTO>> getPersonalTargetsByEmployee(@PathVariable Long idEmployee) {
        try {
            List<PersonalTargetDTO> personalTargets = personalTargetService.getPersonalTargetsByEmployee(idEmployee);
            return ResponseEntity.ok(personalTargets);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/search")
    public List<EmployeeDTO> searchConsultantsByTarget(
            @RequestParam(value = "skill", required = false) String skill,
//            @RequestParam(value = "targetArea", required = false) List<TargetArea> targetAreas,
            @RequestParam(value = "targetStatus", required = false) List<TargetStatus> targetStatuses,
//            @RequestParam(value = "supportedValue", required = false) List<SupportedValue> supportedValues,
            @RequestParam(value = "targetDate", required = false) int targetDate,
            @RequestParam(value = "quarter", required = false) List<String> quarters) {

        return personalTargetService.searchConsultantsByTarget(skill, /*targetAreas,*/ targetStatuses, /*supportedValues,*/ targetDate, quarters);
    }

}









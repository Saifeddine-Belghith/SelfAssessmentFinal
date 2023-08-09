package com.altersis.skillmatrix.targetRole;

import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.altersis.skillmatrix.personaltarget.PersonalTargetDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/target-role")
public class TargetRoleController {
    private final TargetRoleService targetRoleService;

    public TargetRoleController(TargetRoleService targetRoleService) {
        this.targetRoleService = targetRoleService;
    }

    //assign Target Role to employee
    @PostMapping("/assign/{idEmployee}/origins/{originId}")
    public ResponseEntity<TargetRoleDTO> assignPersonalTarget(@RequestBody TargetRoleDTO targetRoleDTO, @PathVariable Long idEmployee, @PathVariable Long originId) {
        TargetRoleDTO assignedTargetRole = targetRoleService.assignTargetRole(targetRoleDTO, idEmployee, originId);
        return ResponseEntity.ok(assignedTargetRole);
    }

    //Delete Target Role
    @DeleteMapping("/{idTargetRole}")
    public ResponseEntity<Void> deleteTargetRole(@PathVariable Long idTargetRole) {
        targetRoleService.deleteTargetRole(idTargetRole);
        return ResponseEntity.noContent().build();
    }

    //Get Target Role by Employee
    @GetMapping("/employee/{idEmployee}")
    public ResponseEntity<List<TargetRoleDTO>> getPersonalTargetsByEmployee(@PathVariable Long idEmployee) {
        try {
            List<TargetRoleDTO> targetRole = targetRoleService.getTargetRoleByEmployee(idEmployee);
            return ResponseEntity.ok(targetRole);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.altersis.skillmatrix.targetRole;

import com.altersis.skillmatrix.employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class TargetRoleDTO {
    private Long idTargetRole;
    private String role;
    private EmployeeDTO originEmployee;
    private String definedBy;
    private EmployeeDTO employee;

    public TargetRoleDTO(TargetRole targetRole) {
        this.idTargetRole = targetRole.getIdTargetRole();

        this.role = targetRole.getRole().getDisplayName();
        this.originEmployee = targetRole.getOriginEmployee() != null ? new EmployeeDTO(targetRole.getOriginEmployee()) : null;
        this.definedBy = targetRole.getDefinedBy();

        this.employee = targetRole.getEmployee() != null ? new EmployeeDTO(targetRole.getEmployee()) : null;

    }

}

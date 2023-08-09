package com.altersis.skillmatrix.targetRole;


import java.util.List;

public interface TargetRoleService {
    public TargetRoleDTO assignTargetRole(TargetRoleDTO targetRoleDTO, Long idEmployee, Long originId);
    void deleteTargetRole(Long idTargetRole);
    public List<TargetRoleDTO> getTargetRoleByEmployee(Long idEmployee);
}

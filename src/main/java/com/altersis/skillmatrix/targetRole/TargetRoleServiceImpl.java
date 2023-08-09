package com.altersis.skillmatrix.targetRole;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeService;
import com.altersis.skillmatrix.enumeration.ProfileRoleName;
import com.altersis.skillmatrix.enumeration.TargetArea;
import com.altersis.skillmatrix.enumeration.TargetStatus;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.altersis.skillmatrix.personaltarget.PersonalTarget;
import com.altersis.skillmatrix.personaltarget.PersonalTargetDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TargetRoleServiceImpl implements TargetRoleService{
    private final TargetRoleRepository targetRoleRepository;

    private final EmployeeService employeeService;

    public TargetRoleServiceImpl(TargetRoleRepository targetRoleRepository, EmployeeService employeeService) {
        this.targetRoleRepository = targetRoleRepository;
        this.employeeService = employeeService;
    }

    //For Manager and Coach
    @Override
    public TargetRoleDTO assignTargetRole(TargetRoleDTO targetRoleDTO, Long idEmployee, Long originId) {
        Employee employee = employeeService.findById(idEmployee);
        if (employee == null) {
            throw new IllegalArgumentException("Employee with ID " + idEmployee + " not found.");
        }
        Employee originEmployee = employeeService.findById(originId);
        if (originEmployee == null) {
            throw new IllegalArgumentException("Origin employee with ID " + originId + " not found.");
        }
        TargetRole targetRole = new TargetRole();
        ProfileRoleName role = ProfileRoleName.valueOf(targetRoleDTO.getRole());
        targetRole.setRole(role);
        targetRole.setEmployee(employee);
        targetRole.setOriginEmployee(originEmployee);

        if (originEmployee.getIsCoach() && !originEmployee.getIsManager()) {
            targetRole.setDefinedBy("Defined by Coach");
        } else if (originEmployee.getIsManager() && !originEmployee.getIsCoach()) {
            targetRole.setDefinedBy("Defined by Manager");
        } else if (originEmployee.getIsManager() && originEmployee.getIsCoach()) {
            targetRole.setDefinedBy("Defined by Manager and Coach");
        }
        TargetRole savedTargetRole = targetRoleRepository.save(targetRole);

        return new TargetRoleDTO(savedTargetRole);

    }

    @Override
    public void deleteTargetRole(Long idTargetRole) {
        targetRoleRepository.deleteById(idTargetRole);
    }

    @Override
    public List<TargetRoleDTO> getTargetRoleByEmployee(Long idEmployee) {
        Employee employee = employeeService.findById(idEmployee);

        List<TargetRole> targetRoles = targetRoleRepository.findAllByEmployeeIdEmployee(idEmployee);
        List<TargetRoleDTO> targetRoleDTOs = new ArrayList<>();

        for (TargetRole targetRole : targetRoles) {
            targetRoleDTOs.add(new TargetRoleDTO(targetRole));
        }

        return targetRoleDTOs;
    }


}

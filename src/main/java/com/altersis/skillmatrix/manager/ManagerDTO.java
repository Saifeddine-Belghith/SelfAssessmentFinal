package com.altersis.skillmatrix.manager;

import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.team.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDTO extends EmployeeDTO {
    private Long idManager;
    private Team team;
}

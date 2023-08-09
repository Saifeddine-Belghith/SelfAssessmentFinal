package com.altersis.skillmatrix.team;

import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.manager.ManagerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private Long id;
    private String name;
    private ManagerDTO manager;
    private List<EmployeeDTO> employees;
}

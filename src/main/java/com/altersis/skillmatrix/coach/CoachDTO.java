package com.altersis.skillmatrix.coach;

import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.enumeration.Experience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CoachDTO extends EmployeeDTO {
    private Long idCoach;
//    private List<CoacheeDTO> coachees;

    public CoachDTO(Long idCoach, Long idEmployee, String firstName, String lastName, String email, String password, String phone, Experience experienceLevel, String role, Object o) {

    }

//    private CoachDTO coachDTO;


}
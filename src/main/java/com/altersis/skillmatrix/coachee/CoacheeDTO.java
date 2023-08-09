package com.altersis.skillmatrix.coachee;

import com.altersis.skillmatrix.assessment.AssessmentDTO;
import com.altersis.skillmatrix.coach.CoachDTO;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.enumeration.Experience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class CoacheeDTO extends EmployeeDTO {
    private Long idCoachee;
    private CoachDTO coach;
    private boolean isCoach;
    private boolean isCoachee;
    private boolean isManager;
    public CoacheeDTO(Long idCoachee, Long idEmployee, String firstName, String lastName, String email, String password,
                      String phone, Experience experienceLevel, String role, boolean isCoach, boolean isCoachee, boolean isManager, CoachDTO coach,
                      List<AssessmentDTO> assessments) {
        super(idEmployee, firstName, lastName, email, password, phone, experienceLevel, role, assessments);
        this.idCoachee = idCoachee;
        this.isCoach = isCoach;
        this.isCoachee = isCoachee;
        this.isManager = isManager;
        this.coach = coach;
    }
}
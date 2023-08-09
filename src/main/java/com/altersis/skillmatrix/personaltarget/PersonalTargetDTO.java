package com.altersis.skillmatrix.personaltarget;

import com.altersis.skillmatrix.employee.EmployeeDTO;
import com.altersis.skillmatrix.enumeration.TargetStatus;
import com.altersis.skillmatrix.skill.Skill;
import com.altersis.skillmatrix.skill.SkillDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalTargetDTO {
    private Long idPersonalTarget;

    private String skill;
    private String description;
    private String acceptanceCriteria;
    private String quarter;
    private String targetArea;
    private TargetStatus status;
    private EmployeeDTO origin;
    private String definedBy;
    private EmployeeDTO employee;

    private int targetDate;
    private String supportedValue;




//    private ClientFeedbackDTO ClientFeedback;
    public PersonalTargetDTO(PersonalTarget personalTarget) {
        this.idPersonalTarget = personalTarget.getIdPersonalTarget();
        this.skill = personalTarget.getSkill();
        this.description = personalTarget.getDescription();
        this.acceptanceCriteria= personalTarget.getAcceptanceCriteria();
        this.targetArea = personalTarget.getTargetArea().getDisplayName();
        this.status = personalTarget.getStatus();
        this.origin = personalTarget.getOrigin() != null ? new EmployeeDTO(personalTarget.getOrigin()) : null;
        this.definedBy = personalTarget.getDefinedBy();

        this.employee = personalTarget.getEmployee() != null ? new EmployeeDTO(personalTarget.getEmployee()) : null;
        this.targetDate = personalTarget.getTargetDate();
        this.quarter= personalTarget.getQuarter();
        this.supportedValue = personalTarget.getSupportedValue().getDisplayName();
    }

}
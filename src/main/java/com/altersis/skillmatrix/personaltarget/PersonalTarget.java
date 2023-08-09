package com.altersis.skillmatrix.personaltarget;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.enumeration.SupportedValue;
import com.altersis.skillmatrix.enumeration.TargetArea;
import com.altersis.skillmatrix.enumeration.TargetStatus;
import com.altersis.skillmatrix.skill.Skill;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersonalTarget;


    private String skill;

    @Column(name = "description")
    private String description;
    @Column(name = "acceptance_criteria")
    private String acceptanceCriteria;
    private String quarter;
    @Enumerated(EnumType.STRING)
    private TargetArea targetArea;
    @Column(name = "target_area_display")
    private String targetAreaDisplay; // New field for targetArea display name

    public void setTargetArea(TargetArea targetArea) {
        this.targetArea = targetArea;
        this.targetAreaDisplay = targetArea.getDisplayName();
    }



    private int targetDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TargetStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "origin")
    private Employee origin ;

    private String definedBy;

    @Enumerated(EnumType.STRING)
    private SupportedValue supportedValue;
    @Column(name = "supported_value_display")
    private String supportedValueDisplay; // New field for targetArea display name
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "target_id")
//    private Target targetArea;


}

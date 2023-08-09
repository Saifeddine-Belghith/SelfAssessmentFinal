package com.altersis.skillmatrix.targetRole;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.enumeration.ProfileRoleName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class TargetRole {
    @Id
    @GeneratedValue
    private Long idTargetRole;
    @Enumerated(EnumType.STRING)
    private ProfileRoleName role;

    @Column(name = "profile_role_name_display")
    private String profileRoleNameDisplay; // New field for role display name

    // constructors, getters, and setters

    public void setRole(ProfileRoleName role) {
        this.role = role;
        this.profileRoleNameDisplay = role.getDisplayName();
    }

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "originEmployee")
    private Employee originEmployee ;

    private String definedBy;

}

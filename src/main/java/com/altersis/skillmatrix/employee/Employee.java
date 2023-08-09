package com.altersis.skillmatrix.employee;

import com.altersis.skillmatrix.ClientFeedback.ClientFeedback;
import com.altersis.skillmatrix.assessment.Assessment;
import com.altersis.skillmatrix.enumeration.Experience;
import com.altersis.skillmatrix.personaltarget.PersonalTarget;
import com.altersis.skillmatrix.targetRole.TargetRole;
import com.altersis.skillmatrix.team.Team;
//import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idEmployee;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Experience experienceLevel;
  //  private String picture;
    //

    private String role;
    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties("employee")
    private List<Assessment> assessments;

    @JsonIgnoreProperties("employee")
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<PersonalTarget> personalTargets;
    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<ClientFeedback> ClientFeedbacks;

    @JsonIgnoreProperties("employee")
    @OneToOne(mappedBy = "employee")
    private TargetRole targetRole;
//    @OneToMany(mappedBy = "originEmployee")
//    private List<TargetRole> myCoacheeTargetRole;

    @OneToMany(mappedBy = "origin")
    private List<PersonalTarget> myCoacheeTargets;
    @Column(name = "is_coach")
    private boolean isCoach;
    @Column(name = "is_manager")
    private boolean isManager;
    @Column(name = "is_coachee")
    private boolean isCoachee;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "manager_id")
//    private Employee manager;

    @JsonIgnoreProperties("employee")
    @ManyToMany(mappedBy = "coaches")
    private List<Employee> coachees;
    @JsonIgnoreProperties("employee")
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
//    @ManyToMany(mappedBy = "employees")
//    private List<Employee> managers;
@JsonIgnoreProperties("employee")
    @OneToMany(mappedBy = "manager")
    private List<Employee> employeesManaged;


    // table of Coach and Coachee
    @JsonIgnoreProperties
    @ManyToMany
    @JoinTable(name = "employee_coach",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "coach_id"))
    private List<Employee> coaches;

    public Employee getCoach() {
        if (this.coaches != null && !this.coaches.isEmpty()) {
            return this.coaches.get(0);
        }
        return null;
    }
    public Long getidCoach() {
        if (this.coaches != null && !this.coaches.isEmpty()) {
            return this.coaches.get(0).getIdEmployee();
        }
        return null;
    }

// team
@ManyToMany
@JoinTable(name = "myteam",
        joinColumns = @JoinColumn(name = "manager_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id"))
private List<Employee> employees;

    public void setCoachees(List<Employee> coachees) {
        if (isCoach ) {
            this.coachees = coachees;
        } else {
            throw new RuntimeException("Only coaches can have coachees");
        }
    }

    @JsonIgnore
    public void setCoach(Employee coach) {
        if (isCoachee || isCoach) {
            this.coaches = new ArrayList<>();
            this.coaches.add(coach);
        } else {
            throw new RuntimeException("Error ");
        }
    }
    @JsonIgnore
    public void setManager(Employee manager) {
        if (getIsManager()) {
            throw new RuntimeException("Only non-managers can have managers");
        }
        this.manager = manager;
        manager.getEmployeesManaged().add(this);
    }

    public void addManager(Employee manager) {
        if (getIsManager()) {
            throw new RuntimeException("Only non-managers can have managers");
        }
        if (this.manager != null) {
            throw new RuntimeException("Employee already has a manager");
        }
        this.manager = manager;
        manager.getEmployees().add(this);
    }


    public void setIsCoach(boolean isCoach) {
        this.isCoach = isCoach;
    }
    public void setIsManager(boolean isManager) {
        this.isManager= isManager;
    }
    public void setIsCoachee(boolean isCoachee) {
        this.isCoachee = isCoachee;
    }

    public boolean getIsCoach() {
        return isCoach;
    }

    public boolean getIsManager() {
        return isManager;
    }

    public boolean getIsCoachee() {
        return isCoachee;
    }
}

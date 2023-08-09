package com.altersis.skillmatrix.employee;

import com.altersis.skillmatrix.assessment.AssessmentDTO;
import com.altersis.skillmatrix.enumeration.Experience;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"coaches", "coachIds"})
public class EmployeeDTO {

    private Long idEmployee;

    private String firstName;

    private String lastName;

    private String email;

    private String password;
    private String phone;
    private Experience experienceLevel;

   // private byte[] picture;

    private String role;
    protected boolean isCoach;
    protected boolean isCoachee;
    protected boolean isManager;
    @JsonIgnore
    private List<AssessmentDTO> assessments;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private EmployeeDTO manager;


    @JsonIgnore
    @ManyToMany(mappedBy = "coaches")
    private List<EmployeeDTO> coachees;

    @JsonIgnore
    private EmployeeDTO coach;

    @JsonIgnore
    private Long idCoach;
    public EmployeeDTO(Employee employee) {
        this.idEmployee = employee.getIdEmployee();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
    }

    @JsonIgnoreProperties
    @ManyToMany
    @JoinTable(name = "employee_coach",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "coach_id"))
    private List<EmployeeDTO> coaches = new ArrayList<>();


    public void setCoachees(List<EmployeeDTO> coachees) {
        if (isCoach) {
            this.coachees = coachees;
        } else {
            throw new RuntimeException("Only coaches can have coachees");
        }
    }
    public void setCoach(EmployeeDTO coach) {
        if (isCoachee || isCoach) {
            this.coaches = new ArrayList<>();
            this.coaches.add(coach);
        } else {
            throw new RuntimeException("Error ");
        }
    }


    public Long getidCoach() {
        if (this.coaches != null && !this.coaches.isEmpty()) {
            return this.coaches.get(0).getIdEmployee();
        }
        return null;
    }
    public EmployeeDTO(Long idEmployee, String firstName, String lastName, String email, String password, String phone, Experience experienceLevel, String role, List<AssessmentDTO> assessments) {
    }

    public boolean getIsCoach() {
        return isCoach;
    }


    public boolean getIsCoachee() {
        return isCoachee;
    }


    public boolean getIsManager() {
        return isManager;
    }

    public void setIsCoach(boolean coach) {
        isCoach = coach;
    }

    public void setIsCoachee(boolean coachee) {
        isCoachee = coachee;
    }

    public void setIsManager(boolean manager) {
        isManager = manager;
    }
    public EmployeeDTO getCoach() {
        if (this.coaches != null && !this.coaches.isEmpty()) {
            return this.coaches.get(0);
        }
        return null;
    }
    public void setFromEntity(Employee employee) {
        this.idEmployee = employee.getIdEmployee();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.password = employee.getPassword();
        this.phone = employee.getPhone();
        this.experienceLevel = employee.getExperienceLevel();
        this.role = employee.getRole();
        this.isCoach = employee.getIsCoach();
        this.isCoachee = employee.getIsCoachee();
        this.isManager = employee.getIsManager();
        this.coaches = employee.getCoaches().stream().map(EmployeeDTO::new).collect(Collectors.toList());
        this.coachees = employee.getCoachees().stream().map(EmployeeDTO::new).collect(Collectors.toList());
    }

}


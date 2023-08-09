package com.altersis.skillmatrix.coach;

import com.altersis.skillmatrix.employee.Employee;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Coach extends Employee {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCoach;
//    @OneToMany(mappedBy = "coach")
//    private List<Coachee> coachees;
//    @OneToOne(optional = true)
//    private Coach coach;
}

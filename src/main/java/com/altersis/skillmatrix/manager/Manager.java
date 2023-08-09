package com.altersis.skillmatrix.manager;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.team.Team;
//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
/*@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)*/
public class Manager extends Employee {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idManager;
    @OneToOne(mappedBy = "manager")
    @JoinColumn(name = "team_id")
    private Team team;
}

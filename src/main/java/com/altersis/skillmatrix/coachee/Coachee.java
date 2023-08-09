package com.altersis.skillmatrix.coachee;

import com.altersis.skillmatrix.coach.Coach;
import com.altersis.skillmatrix.employee.Employee;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(callSuper = true)
@Table(name = "Coachees", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "coach_id"}))
public class Coachee extends Employee {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCoachee;
    @ManyToOne
    @JoinColumn(name="coach_id")
    private Coach coach;


}

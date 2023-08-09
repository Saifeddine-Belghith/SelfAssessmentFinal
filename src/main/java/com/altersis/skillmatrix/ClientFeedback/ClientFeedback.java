package com.altersis.skillmatrix.ClientFeedback;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.enumeration.Evaluation;
import com.altersis.skillmatrix.enumeration.TargetArea;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idClientFeedback;

    private String clientName;

    @Enumerated(EnumType.STRING)
    private Evaluation evaluation ;

    private double rating;


    private String trend;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;
    @Column(name = "evaluation_display")
    private String evaluationDisplay; // New field for Evaluation display name
    private String comment;
    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
        this.evaluationDisplay = evaluation.getDisplayName();
    }
}

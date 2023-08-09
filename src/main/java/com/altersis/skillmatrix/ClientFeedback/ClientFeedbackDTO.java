package com.altersis.skillmatrix.ClientFeedback;

import com.altersis.skillmatrix.employee.Employee;
import com.altersis.skillmatrix.employee.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientFeedbackDTO {
    private Long idClientFeedback;

    private String clientName;
    private String evaluation;
    private double rating;
    private String trend;

    private EmployeeDTO employee;

    private String comment;
    public ClientFeedbackDTO(ClientFeedback clientFeedback) {
        this.idClientFeedback = clientFeedback.getIdClientFeedback();
        this.clientName=clientFeedback.getClientName();
        this.evaluation = clientFeedback.getEvaluation() != null ? clientFeedback.getEvaluation().getDisplayName() : null;
        this.rating = clientFeedback.getRating();
        this.trend = clientFeedback.getTrend();
        this.employee = clientFeedback.getEmployee() != null ? new EmployeeDTO(clientFeedback.getEmployee()) : null;
        this.comment = clientFeedback.getComment();
    }
}
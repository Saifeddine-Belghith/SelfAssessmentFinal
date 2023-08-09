package com.altersis.skillmatrix.assessment;

import java.util.List;

public class EmployeeAssessmentDTO {
    private Long idEmployee;
    private List<AssessmentDTO> assessments;

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public List<AssessmentDTO> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<AssessmentDTO> assessments) {
        this.assessments = assessments;
    }
}

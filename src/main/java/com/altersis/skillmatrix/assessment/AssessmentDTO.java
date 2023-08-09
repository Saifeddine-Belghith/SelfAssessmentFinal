package com.altersis.skillmatrix.assessment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDTO {

    private Long idAssessment;

    private Long idEmployee;

    private Long idSkill;

    /*@Range(min = 0, max = 3)*/
    private Integer rating;
  //  private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    private Date assessmentDate;
}

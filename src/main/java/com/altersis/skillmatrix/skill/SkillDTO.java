package com.altersis.skillmatrix.skill;

import com.altersis.skillmatrix.assessment.AssessmentDTO;
import com.altersis.skillmatrix.category.CategoryDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillDTO {

    private Long idSkill;

    private String skillName;
    private String description;
    @JsonIgnore
    private List<AssessmentDTO> assessments;
    @JsonIgnoreProperties(value = {"skills"}, allowSetters = true)
    private CategoryDTO category;
}

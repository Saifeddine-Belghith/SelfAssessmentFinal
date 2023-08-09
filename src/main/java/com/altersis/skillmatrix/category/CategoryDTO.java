package com.altersis.skillmatrix.category;

import com.altersis.skillmatrix.skill.SkillDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class CategoryDTO {
    private Long idCategory;
    private String categoryName;
   // private String description;

    @JsonIgnoreProperties(value = {"category"}, allowSetters = true)
    private List<SkillDTO> skills;
}
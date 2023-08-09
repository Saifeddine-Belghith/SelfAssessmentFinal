package com.altersis.skillmatrix.skill;
/*
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.altersis.skillmatrix.category.entities.Category;
import com.altersis.skillmatrix.category.payloads.CategoryDTO;
import com.altersis.skillmatrix.category.repositories.CategoryRepository;
import com.altersis.skillmatrix.category.services.CategoryService;
*/
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


public interface SkillService {

    //Get all skills
    @JsonIgnoreProperties(value = {"skills"}, allowSetters = true)
    List<SkillDTO> viewAll();

    SkillDTO createSkill(SkillDTO skillDto, Long catid);

    //Get skill by id
    SkillDTO viewSkillById(Long idSkill);

    //Delete skill

    void deleteSkillById(Long skillId);


    //Update skill for Administrator
    SkillDTO updateSkill(Long skillId, SkillDTO updatedSkill);

    List<SkillDTO> findSkillByCategory(Long catId);
    public SkillDTO getSkillById(Long id);
/*
    //Update skill Note field for Employee
    public SkillDto updateSkillNote(Long skillId, SkillDto updatedSkill){
        Skill oldSkill = skillRepository.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));
      /*  oldSkill.setSkillName(updatedSkill.getSkillName());
        oldSkill.setDescription(updatedSkill.getDescription()); *//*
        oldSkill.setNote(updatedSkill.getNote());
        Skill save= skillRepository.save(oldSkill);
        SkillDto skillDto = toDto(save);
        return skillDto;
    }
*/
    //Find skill by category
 

    //Convert SkillDto to Skill
    Skill toEntity(SkillDTO skillDto);

    //Convert Skill to SkillDto

    SkillDTO toDto(Skill skill);

   // List<SkillDTO> findSkillByCategory(Long catId);

}
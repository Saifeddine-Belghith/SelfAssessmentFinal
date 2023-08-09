package com.altersis.skillmatrix.skill;

import com.altersis.skillmatrix.assessment.Assessment;
import com.altersis.skillmatrix.assessment.AssessmentDTO;
import com.altersis.skillmatrix.category.Category;
import com.altersis.skillmatrix.category.CategoryDTO;
import com.altersis.skillmatrix.category.CategoryRepository;
import com.altersis.skillmatrix.exception.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService{


    private final SkillRepository skillRepository;

      private final CategoryRepository categoryRepository;
      @Autowired
      public SkillServiceImpl(SkillRepository skillRepository, CategoryRepository categoryRepository) {
          this.skillRepository = skillRepository;
          this.categoryRepository = categoryRepository;
      }
    public SkillDTO createSkill(SkillDTO skillDto, Long catid){
        //Fetch Category is available or not
           Category category = this.categoryRepository.findById(catid).orElseThrow(()-> new ResourceNotFoundException("Category not found with id: " + catid));
        //SkillDto to Skill
        Skill skill = toEntity(skillDto);
        skill.setCategory(category);
        Skill newSkill = skillRepository.save(skill);

        //Skill to SkillDto
        SkillDTO sDto = toDto(newSkill);
        return sDto;
    }

    //Get all skills
    @JsonIgnoreProperties(value = {"skills"}, allowSetters = true)
    public List<SkillDTO> viewAll(){
        //SkillDto to Skill
        List<Skill> findAll = skillRepository.findAll();
        //Skill to skillDto
        List<SkillDTO> findAllDto = findAll.stream().map(skill -> this.toDto(skill)).collect(Collectors.toList());
        return findAllDto;
    }

    //Get skill by id
    public SkillDTO viewSkillById(Long idSkill ){
        Skill findById = skillRepository.findById(idSkill).orElseThrow(()-> new ResourceNotFoundException("Skill not found with id: " + idSkill));
        SkillDTO skillDto = toDto(findById);
        return skillDto;
    }
    @Override
    public SkillDTO getSkillById(Long id) {
        Skill findById = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Skill not found"));
        SkillDTO skillDto = toDto(findById);
        return skillDto;
    }

    //Delete skill

    public void deleteSkillById(Long skillId){
        Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));
        skillRepository.deleteById(skillId);
    }



    //Update skill for Administrator
    public SkillDTO updateSkill(Long skillId, SkillDTO updatedSkill){
        Skill oldSkill = skillRepository.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + skillId));
        oldSkill.setSkillName(updatedSkill.getSkillName());
        oldSkill.setDescription(updatedSkill.getDescription());
        Skill save= skillRepository.save(oldSkill);
        SkillDTO skillDto = toDto(save);
        return skillDto;
    }
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
    public List<SkillDTO> findSkillByCategory(Long idCategory){
        Category Cat = this.categoryRepository.findById(idCategory).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + idCategory));
        List<Skill> findByCategory = this.skillRepository.findByCategory(Cat);
        List<SkillDTO> collect =  findByCategory.stream().map(skill -> toDto(skill)).collect(Collectors.toList());
        return collect;
    }

    //Convert SkillDto to Skill
    public Skill toEntity(SkillDTO skillDto) {
        Skill skill = new Skill();
        skill.setIdSkill(skillDto.getIdSkill());
        skill.setSkillName(skillDto.getSkillName());
        skill.setDescription(skillDto.getDescription());
        return skill;
    }

    //Convert Skill to SkillDto

    public SkillDTO toDto(Skill skill) {
        SkillDTO skillDto = new SkillDTO();
        skillDto.setIdSkill(skill.getIdSkill());
        skillDto.setSkillName(skill.getSkillName());
        skillDto.setDescription(skill.getDescription());

        //Change Category to CategoryDto
        CategoryDTO catDto=new CategoryDTO();
        catDto.setIdCategory ( skill.getCategory().getIdCategory());
        catDto.setCategoryName(skill.getCategory().getCategoryName());
        //catDto.setDescription(skill.getCategory().getDescription());

        //Then Set CategoryDto in skillDto
        skillDto.setCategory(catDto);
        return skillDto;
    }
}
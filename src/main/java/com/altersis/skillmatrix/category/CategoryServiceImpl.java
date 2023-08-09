package com.altersis.skillmatrix.category;

import com.altersis.skillmatrix.exception.ResourceNotFoundException;

import com.altersis.skillmatrix.skill.Skill;
import com.altersis.skillmatrix.skill.SkillDTO;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDto) {
        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
       // category.setDescription(categoryDto.getDescription());

        Category savedCategory = this.categoryRepository.save(category);

        CategoryDTO savedCategoryDto = new CategoryDTO();
        savedCategoryDto.setIdCategory(savedCategory.getIdCategory());
        savedCategoryDto.setCategoryName(savedCategory.getCategoryName());
    //    savedCategoryDto.setDescription(savedCategory.getDescription());

        return savedCategoryDto;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO newCategory, Long idCategory) {
        Category category = this.categoryRepository.findById(idCategory)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + idCategory));

        category.setCategoryName(newCategory.getCategoryName());
      //  category.setDescription(newCategory.getDescription());

        Category updatedCategory = this.categoryRepository.save(category);

        CategoryDTO updatedCategoryDto = new CategoryDTO();
        updatedCategoryDto.setIdCategory(updatedCategory.getIdCategory());
        updatedCategoryDto.setCategoryName(updatedCategory.getCategoryName());
       // updatedCategoryDto.setDescription(updatedCategory.getDescription());

        return updatedCategoryDto;
    }

    @Override
    public void deleteCategory(Long idCategory) {
        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + idCategory));

        List<Skill> skills = category.getSkills();

        for (Skill skill : skills) {
            skill.setCategory(null);
        }

        skills.clear();
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDTO getCategoryById(Long idCategory) {
        Category category = this.categoryRepository.findById(idCategory)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id : " + idCategory));

        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setIdCategory(category.getIdCategory());
        categoryDto.setCategoryName(category.getCategoryName());
    //    categoryDto.setDescription(category.getDescription());
        List<SkillDTO> skillDtoList = new ArrayList<>();
        for (Skill skill : category.getSkills()) {
            SkillDTO skillDto = new SkillDTO();
            skillDto.setIdSkill(skill.getIdSkill());
            skillDto.setSkillName(skill.getSkillName());

            skillDto.setDescription(skill.getDescription());
            /*      skillDto.setCategoryName(skill.getCategory().getCategoryName()); */
            skillDtoList.add(skillDto);
        }

        categoryDto.setSkills(skillDtoList);

        return categoryDto;
    }

    @Override
    public List<CategoryDTO> getAll() {
        List<Category> categories = this.categoryRepository.findAll();
        List<CategoryDTO> categoryDtos = new ArrayList<>();

        for (Category category : categories) {
            CategoryDTO categoryDto = new CategoryDTO();
            categoryDto.setIdCategory(category.getIdCategory());
            categoryDto.setCategoryName(category.getCategoryName());
         //   categoryDto.setDescription(category.getDescription());

            List<SkillDTO> skillDtoList = new ArrayList<>();
            for (Skill skill : category.getSkills()) {
                SkillDTO skillDto = new SkillDTO();
                skillDto.setIdSkill(skill.getIdSkill());
                skillDto.setSkillName(skill.getSkillName());
                skillDto.setDescription(skill.getDescription());
                /*      skillDto.setCategoryName(skill.getCategory().getCategoryName()); */
                skillDtoList.add(skillDto);
            }

            categoryDto.setSkills(skillDtoList);

            categoryDtos.add(categoryDto);
        }

        return categoryDtos;
    }

    @Override
    public CategoryDTO getCategoryByIdWithSkills(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setIdCategory(category.getIdCategory());
        categoryDto.setCategoryName(category.getCategoryName());
      //  categoryDto.setDescription(category.getDescription());

        List<SkillDTO> skillDtoList = new ArrayList<>();
        for (Skill skill : category.getSkills()) {
            SkillDTO skillDto = new SkillDTO();
            skillDto.setIdSkill(skill.getIdSkill());
            skillDto.setSkillName(skill.getSkillName());
            skillDto.setDescription(skill.getDescription());
      /*      skillDto.setCategoryName(skill.getCategory().getCategoryName()); */
            skillDtoList.add(skillDto);
        }

        categoryDto.setSkills(skillDtoList);
        return categoryDto;
    }
}



package com.altersis.skillmatrix.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //Get all category
    @JsonIgnoreProperties(value = {"category"}, allowSetters = true)
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categoryDtos = categoryService.getAll();
        return ResponseEntity.ok(categoryDtos);
    }

    //Get category with skills
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("id") Long categoryId) {
        CategoryDTO categoryDto = categoryService.getCategoryByIdWithSkills(categoryId);
        return ResponseEntity.ok(categoryDto);
    }

    //Create a new category
    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDto) {
        CategoryDTO createdCategoryDto = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(createdCategoryDto);
    }

    //Update category
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("id") Long categoryId, @RequestBody CategoryDTO categoryDto) {
        CategoryDTO updatedCategoryDto = categoryService.updateCategory(categoryDto, categoryId);
        return ResponseEntity.ok(updatedCategoryDto);
    }

    //Delete a category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

   /* @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("id") Long categoryId) {
        CategoryDTO categoryDto = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(categoryDto);
    }*/
}


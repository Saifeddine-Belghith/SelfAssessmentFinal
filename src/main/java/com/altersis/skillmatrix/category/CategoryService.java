package com.altersis.skillmatrix.category;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDto);

    CategoryDTO updateCategory(CategoryDTO newCategory, Long idCategory);

    void deleteCategory(Long idCategory);

    CategoryDTO getCategoryById(Long idCategory);

    List<CategoryDTO> getAll();

    CategoryDTO getCategoryByIdWithSkills(Long categoryId);
}

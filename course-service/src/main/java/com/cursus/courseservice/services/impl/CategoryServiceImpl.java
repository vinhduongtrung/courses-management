package com.cursus.courseservice.services.impl;

import com.cursus.courseservice.dtos.Mapper;
import com.cursus.courseservice.dtos.request.category.CategoryDTO;
import com.cursus.courseservice.dtos.response.category.CategoryDetail;
import com.cursus.courseservice.dtos.response.category.CategoryList;
import com.cursus.courseservice.entities.Category;
import com.cursus.courseservice.repositories.CategoryRepository;
import com.cursus.courseservice.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Override
    public CategoryList getAll(int page, int limit, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<Category> categoryPage = categoryRepository.findByNameContaining(pageRequest, keyword);

        List<CategoryDetail> categoryDetailList = categoryPage.getContent().stream()
                .map(Mapper::categoryMapper)
                .collect(Collectors.toList());

        return CategoryList.builder()
                .categories(categoryDetailList)
                .totalPages(categoryPage.getTotalPages())
                .build();
    }

    @Override
    public CategoryDetail getById(Long id) {
        return categoryRepository.findById(id)
                .map(Mapper::categoryMapper)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id)
                );
    }

    @Override
    @Transactional
    public Set<CategoryDetail> save(Set<CategoryDTO> categoryDTOSet) {
        Set<Category> categoriesToSave = categoryDTOSet.stream()
                .map(categoryDTO -> {
                    Long parentId = categoryDTO.parentCategoryId();
                    Category parentCategory = categoryRepository.findById(parentId).orElse(null);
                    return Category.builder()
                            .name(categoryDTO.name())
                            .parentCategory(parentCategory)
                            .build();
                })
                .collect(Collectors.toSet());

        List<Category> savedCategories = categoryRepository.saveAll(categoriesToSave);

        return savedCategories.stream()
                .map(Mapper::categoryMapper)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }
}

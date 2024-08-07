package com.cursus.courseservice.services;

import com.cursus.courseservice.dtos.request.category.CategoryDTO;
import com.cursus.courseservice.dtos.response.category.CategoryDetail;
import com.cursus.courseservice.dtos.response.category.CategoryList;

import java.util.List;
import java.util.Set;


public interface CategoryService {
    CategoryList getAll(int page, int limit, String keyword);
    CategoryDetail getById(Long id);
    Set<CategoryDetail> save(Set<CategoryDTO> categoryDTOSet);
    void delete(Long id);
}

package com.cursus.courseservice.repositories;

import com.cursus.courseservice.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByNameContaining(PageRequest pageRequest, String keyword);

    Optional<Category> findByName(String categoryName);
}

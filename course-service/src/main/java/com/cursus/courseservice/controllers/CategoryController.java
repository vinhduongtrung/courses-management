package com.cursus.courseservice.controllers;

import com.cursus.courseservice.dtos.request.category.CategoryDTO;
import com.cursus.courseservice.services.CategoryService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> getAllCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok().body(String.format("getAllCategories, page=%d, limit=%d",page,limit));
    }

    @PostMapping("")
    public ResponseEntity<?> saveCategories(@Valid @RequestBody CategoryDTO categoryDTO,
                                              BindingResult result) {
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        return ResponseEntity.ok().body("bulk insert " + categoryDTO.name());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id) {
        return ResponseEntity.ok().body("update " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok().body("delete " + id);
    }

    @PostMapping("/fake-generated")
    public ResponseEntity<String> generateFakeCategories() {
        Faker faker = new Faker();
        Set<CategoryDTO> categoryDTOSet = new LinkedHashSet<>();
        for(int i = 0; i < 19; i++) {
            String name = faker.commerce().department();
            categoryDTOSet.add(new CategoryDTO(name,0L));
        }
        categoryService.save(categoryDTOSet);
        return ResponseEntity.ok().body("generate successfully");
    }
}

package com.cursus.courseservice.dtos.request.course;

import com.cursus.courseservice.dtos.request.category.CategoryDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.Set;


public record CourseDTO(
        @NotBlank
        @Size(min = 3, max = 200)
        String name,
        @Min(value = 0)
        @Max(value = 10000000)
        Double price,
        @Min(value = 0)
        @Max(value = 100)
        Double discount,
        String thumbnail,
        @JsonProperty("category_set")
        Set<CategoryDTO> categorySet) {
}

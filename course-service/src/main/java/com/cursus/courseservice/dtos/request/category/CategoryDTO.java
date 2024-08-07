package com.cursus.courseservice.dtos.request.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
        @NotBlank String name,
        @JsonProperty("parent_category_id")
        Long parentCategoryId
        ) {
}

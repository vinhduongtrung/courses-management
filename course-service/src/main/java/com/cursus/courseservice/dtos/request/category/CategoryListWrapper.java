package com.cursus.courseservice.dtos.request.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListWrapper {
    @Valid
    @Size(min = 1, message = "must be contain at least one category")
    private List<CategoryDTO> categories;
}

package com.cursus.courseservice.dtos.response.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDetail {

    private Long id;

    private String name;

    @JsonProperty("parent_category_id")
    private Long parentCategoryId;
}
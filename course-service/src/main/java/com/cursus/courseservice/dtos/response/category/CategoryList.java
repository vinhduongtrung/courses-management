package com.cursus.courseservice.dtos.response.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryList {

    private List<CategoryDetail> categories;

    @JsonProperty("total_pages")
    private int totalPages;
}

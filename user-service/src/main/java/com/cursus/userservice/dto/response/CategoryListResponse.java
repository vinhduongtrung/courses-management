package com.cursus.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryListResponse {

    private List<CategoryDetail> categories;

    @JsonProperty("total_pages")
    private int totalPages;
}

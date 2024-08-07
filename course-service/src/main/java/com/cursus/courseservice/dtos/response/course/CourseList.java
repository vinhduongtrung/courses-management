package com.cursus.courseservice.dtos.response.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CourseList {

    private List<CourseDetail> courses;

    @JsonProperty("total_pages")
    private int totalPages;
}

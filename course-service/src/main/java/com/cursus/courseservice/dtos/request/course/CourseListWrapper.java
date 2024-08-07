package com.cursus.courseservice.dtos.request.course;

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
public class CourseListWrapper {
    @Valid
    @Size(min = 1, message = "must be contain at least one course")
    private List<CourseDTO> courses;
}

package com.cursus.userservice.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Long studentId;
    private Long courseId;
    private String status;


}

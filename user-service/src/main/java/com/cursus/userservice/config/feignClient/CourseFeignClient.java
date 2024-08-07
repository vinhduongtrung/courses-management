package com.cursus.userservice.config.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "http://localhost:3333", path="/api/v1/courses")
public interface CourseFeignClient {

    @PostMapping("/{id}/assign/student/{studentId}")
    void assignStudentToCourse(@PathVariable String id, @PathVariable String studentId);
}

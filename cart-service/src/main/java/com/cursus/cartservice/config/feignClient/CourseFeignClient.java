package com.cursus.cartservice.config.feignClient;

import com.cursus.cartservice.dtos.CourseDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "http://localhost:3333", path="/api/v1/courses")
public interface CourseFeignClient {

    @GetMapping("/{id}")
    CourseDetail getById(@PathVariable Long id);
}

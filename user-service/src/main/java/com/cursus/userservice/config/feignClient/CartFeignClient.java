package com.cursus.userservice.config.feignClient;

import com.cursus.userservice.dto.response.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "http://localhost:4444", path = "/api/v1/cart")
public interface CartFeignClient {

    @GetMapping("/{id}")
    CartResponse viewCart(@PathVariable("id") Long studentId);


    @PostMapping("/{id}/course/{courseId}")
    String addCourseToCart(@PathVariable Long courseId,
                         @PathVariable("id") Long studentId);

    @DeleteMapping("/{id}/course/{courseId}")
    String removeCourseFromCart(@PathVariable Long courseId,
                              @PathVariable("id") Long studentId);
}

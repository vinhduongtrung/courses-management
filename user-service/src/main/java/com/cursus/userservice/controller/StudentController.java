package com.cursus.userservice.controller;


import com.cursus.userservice.config.feignClient.CartFeignClient;
import com.cursus.userservice.config.feignClient.CourseFeignClient;
import com.cursus.userservice.dto.CartDTO;
import com.cursus.userservice.dto.response.CartResponse;
import com.cursus.userservice.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
public class StudentController {

    private CourseFeignClient courseFeignClient;

    private StudentService studentService;

    private CartFeignClient cartFeignClient;

    @PostMapping("/purchase-courses")
    public ResponseEntity<String> purchaseCourses(@Valid @RequestBody CartDTO cartDTO) {
        studentService.purchaseCourses(cartDTO);
        return ResponseEntity.ok().body("purchase course successfully");
    }


    @PostMapping("/{id}/report-course/{courseId}")
    public void reportCourse(@PathVariable Long id, @PathVariable Long courseId, @PathVariable String reportReason) {

    }

    @PostMapping("/{id}/enroll-course/{courseId}")
    public ResponseEntity<String> enrollCourse(@PathVariable String id, @PathVariable String courseId) {
        studentService.enrollCourse(id, courseId);
        return ResponseEntity.ok().body("enroll into course successfully");
    }

    @GetMapping("/{id}/instructors")
    public void viewInstructors(@PathVariable String id) {
        courseFeignClient.assignStudentToCourse("1", "1");
    }

    @GetMapping("/{id}/cart")
    public ResponseEntity<CartResponse> viewCart(@PathVariable("id") Long studentId) {
        return ResponseEntity.ok().body(cartFeignClient.viewCart(studentId));
    }

    @PostMapping("/{id}/cart/course/{courseId}")
    public ResponseEntity<String> addCourseToCart(@PathVariable Long courseId,
                                                  @PathVariable("id") Long studentId) {
        return ResponseEntity.ok().body(cartFeignClient.addCourseToCart(courseId, studentId));
    }


    @DeleteMapping("/{id}/cart/course/{courseId}")
    public ResponseEntity<String> removeCourseFromCart(@PathVariable Long courseId,
                                                       @PathVariable("id") Long studentId) {
        return ResponseEntity.ok().body(cartFeignClient.removeCourseFromCart(courseId, studentId));
    }
}

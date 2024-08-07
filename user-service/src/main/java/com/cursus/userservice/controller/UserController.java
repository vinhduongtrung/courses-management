package com.cursus.userservice.controller;

import com.cursus.userservice.dto.CartDTO;
import com.cursus.userservice.dto.LoginDTO;
import com.cursus.userservice.service.JwtService;
import com.cursus.userservice.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UserController {

    private StudentService studentService;

    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO userLoginRequest, HttpServletResponse response){
        return ResponseEntity.ok().body(jwtService.generateAccessToken(userLoginRequest.getEmail()));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok().body("logout");
    }


    // SETTING
    @GetMapping("/{id}/profile")
    public void viewProfile(@PathVariable Long id) {
        // Logic to get profile analytics for a student
    }

    @PutMapping("/{id}/profile")
    public void editProfile(@PathVariable Long id) {
        // Logic to get profile analytics for a student
    }

    @PatchMapping("/{id}/password")
    public void changePassword(@PathVariable Long id) {

    }

    @GetMapping("/earnings")
    public void viewEarnings() {
        // Logic to view earnings

    }

    @PostMapping("/payout")
    public void payout() {
        // Logic to handle payout
    }





    // CART
    @GetMapping("/{id}/cart")
    public void viewCart(@PathVariable String id) {
        // Implement logic to view courses
    }

    @PostMapping("/{id}/cart/{courseId}")
    public void addCourseToCart(@PathVariable String id, @PathVariable String courseId) {
        // Implement logic to view courses
    }

    @PutMapping("/{id}/cart")
    public void editCart(@PathVariable String id) {
        // Implement logic to view courses
    }

    @DeleteMapping("/{id}/cart/{courseId}")
    public void removeCourseFromCart(@PathVariable String id, @PathVariable String courseId) {
        // Implement logic to view courses
    }





    // COURSES

    @PostMapping("/{id}/course")
    public void submitCourse(@PathVariable String id){

    }

//    @PatchMapping("/{id}/course")
//    public void approveCreateCourse(@PathVariable String id){
//
//    }
//
//    @DeleteMapping("/{id}/course")
//    public void rejectCreateCourse(@PathVariable String id){
//
//    }
//
//    @PatchMapping("/{id}/course")
//    public void blockCourse(@PathVariable String id){
//
//    }
//
//    @PatchMapping("/{id}/course")
//    public void unblockCourse(@PathVariable String id){
//
//    }

    @GetMapping("/{id}/courses")
    public void viewListCourses(@PathVariable String id) {
        // Implement logic to view courses
    }

    @GetMapping("/{id}/course/{courseId}")
    public void viewTrackingCourse(@PathVariable String id, @PathVariable String courseId) {
    }

    @PutMapping("/{id}/course/{courseId}")
    public void editCourse(@PathVariable String id, @PathVariable String courseId) {
        // Implement logic to view courses
    }

    @DeleteMapping("/{id}/course/{courseId}")
    public void deleteCourse(@PathVariable String id, @PathVariable String courseId) {
        // Implement logic to view courses
    }


//    @PatchMapping("/{id}/enroll/{courseId}")
//    public void enrollCourse(@PathVariable String id, @PathVariable String courseId) {
//        studentService.enrollCourse(id, courseId);
//    }

    @PostMapping("/purchase-courses")
    public void purchaseCourses(@Valid @RequestBody CartDTO cartDTO) {
        studentService.purchaseCourses(cartDTO);
    }







    @PatchMapping("/{id}/course/{courseId}/review")
    public void reviewCourse(@PathVariable String id, @PathVariable String courseId) {
    }

    @PatchMapping("/{id}/course/{courseId}/report")
    public void reportCourse(@PathVariable String id, @PathVariable String courseId) {
    }

    @PatchMapping("/{id}/course/{courseId}/saved")
    public void savedCourse(@PathVariable String id, @PathVariable String courseId) {
    }

    // Dashboard for INSTRUCTOR

    @GetMapping("/{id}/student/{studentId}/analytics")
    public void viewStudentAnalytics(@PathVariable String id, @PathVariable String studentId) {
        // Implement logic to view courses
    }




}

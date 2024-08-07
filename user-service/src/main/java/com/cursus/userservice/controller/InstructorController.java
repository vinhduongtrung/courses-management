package com.cursus.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/instructors")
@AllArgsConstructor
public class InstructorController {

    @GetMapping("/{id}/total-students")
    public void getTotalStudents() {
        // Logic to get the total number of students
    }

    @GetMapping("/{id}/total-courses")
    public void getTotalCourses() {
        // Logic to get the total number of courses
    }

    @GetMapping("/{id}/profile-analytics")
    public void getProfileAnalytics(@PathVariable Long id) {
        // Logic to get profile analytics for a student
    }


    @GetMapping("/earnings")
    public void viewEarnings() {
        // Logic to view earnings

    }

    @PostMapping("/payout")
    public void payout() {
        // Logic to handle payout
    }
}

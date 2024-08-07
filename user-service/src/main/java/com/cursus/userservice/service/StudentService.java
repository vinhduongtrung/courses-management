package com.cursus.userservice.service;

import com.cursus.userservice.dto.CartDTO;
import com.cursus.userservice.entity.Student;

public interface StudentService {
    void viewCourses(Student student);
    void viewInstructors(Student student);
    void enrollCourse(String studentId, String courseId);
    void reportCourse(Student student, String courseId, String reportReason);
    void purchaseCourses(CartDTO cartDTO);

    void viewCart(String studentId);
    void addCourseToCart(String studentId, String courseId);
    void removeCourseFromCart(String studentId, String courseId);


//    void viewCourseEnrolled();
//
//    void viewProfile();
//
//    void editProfile();
//
//    void changePassword();
//
//    void crudShoppingCart();
//
//    void purchaseCourse();
//
//    void enrollIntoCourse();
//
//    void viewTrackingOfCourse();
//
//    void reviewCourse();
//
//    void reportCourse();
//
//    void savedCourse();
}

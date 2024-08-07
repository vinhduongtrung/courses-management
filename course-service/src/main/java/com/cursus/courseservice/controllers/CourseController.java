package com.cursus.courseservice.controllers;

import com.cursus.courseservice.dtos.request.course.CourseDTO;
import com.cursus.courseservice.dtos.request.course.CourseListWrapper;
import com.cursus.courseservice.dtos.response.course.CourseDetail;
import com.cursus.courseservice.dtos.response.course.CourseList;
import com.cursus.courseservice.entities.Course;
import com.cursus.courseservice.services.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/api/v1/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/{id}/assign/student/{studentId}")
    public ResponseEntity<?> assignStudentToCourse(@PathVariable String id, @PathVariable String studentId){
        courseService.assignStudentToCourse(id, studentId);
        return ResponseEntity.ok().body("assign student id: " + studentId + "to course : " + id);
    }
    @GetMapping("")
    public ResponseEntity<CourseList> getAllCourses(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        CourseList courseList =  courseService.getAll(page, limit, keyword);
        return ResponseEntity.ok().body(courseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetail> getById(@PathVariable Long id) {
        CourseDetail courseDetail =  courseService.getById(id);
        return ResponseEntity.ok().body(courseDetail);
    }

//    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping
    public ResponseEntity<?> insertCourses(@Valid @RequestBody CourseListWrapper courseList,
//                                           @RequestPart("file") MultipartFile file,
                                           BindingResult result) {
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
//        if(file.getSize() > 10 * 10 * 1024) {
//            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large, maximum is 10MB");
//        }
        List<CourseDetail> courseDetails = courseService.save(courseList.getCourses());
        return ResponseEntity.ok().body(courseDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@Valid @RequestBody CourseDTO courseDTO,
                                           @PathVariable Long id,
                                           BindingResult result) {
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        CourseDetail courseDetail =  courseService.update(courseDTO, id);
        return ResponseEntity.ok().body(courseDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok().body("delete successfully: " + id);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Void> submitCourseForApproval(@PathVariable Long id) {
        // Logic to submit a course for approval
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<?> getCourseAnalytics(@PathVariable Long id) {
        // Logic to get analytics for a course
        return new ResponseEntity<>("analytics", HttpStatus.OK);
    }

}

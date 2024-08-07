package com.cursus.courseservice.services;

import com.cursus.courseservice.dtos.request.course.CourseDTO;
import com.cursus.courseservice.dtos.response.course.CourseDetail;
import com.cursus.courseservice.dtos.response.course.CourseList;

import java.util.List;

public interface CourseService {

    void assignStudentToCourse(String courseId, String studentId);
    CourseList getAll(int page, int limit, String keyword);
    CourseDetail getById(Long id);
    List<CourseDetail> save(List<CourseDTO> courseDTOList);

    CourseDetail update(CourseDTO courseDTO, Long id);
    void delete(Long id);
}

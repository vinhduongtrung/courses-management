package com.cursus.courseservice.repositories;

import com.cursus.courseservice.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByNameContaining(PageRequest pageRequest, String keyword);
}

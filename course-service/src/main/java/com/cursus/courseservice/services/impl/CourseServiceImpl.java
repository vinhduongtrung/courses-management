package com.cursus.courseservice.services.impl;

import com.cursus.courseservice.dtos.Mapper;
import com.cursus.courseservice.dtos.request.category.CategoryDTO;
import com.cursus.courseservice.dtos.request.course.CourseDTO;
import com.cursus.courseservice.dtos.response.course.CourseDetail;
import com.cursus.courseservice.dtos.response.course.CourseList;
import com.cursus.courseservice.entities.Category;
import com.cursus.courseservice.entities.Course;
import com.cursus.courseservice.repositories.CategoryRepository;
import com.cursus.courseservice.repositories.CourseRepository;
import com.cursus.courseservice.services.CourseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;

    private CategoryRepository categoryRepository;

    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void assignStudentToCourse(String courseId, String studentId) {
        System.out.println(Long.parseLong(courseId) + Long.parseLong(studentId));

        //
        try {
            Map<String, Object> props = new HashMap<>() {{
                put("studentId", studentId);
                put("courseId", courseId);
                put("status", "ASSIGNED");
            }};
            // convert Map of properties to String and send it to Kafka
            ObjectMapper objectMapper = new ObjectMapper();
            String mapAsString = objectMapper.writeValueAsString(props);
            kafkaTemplate.send("notification-topic", mapAsString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("cannot assign student to course");
        }
    }

    @Override
    public CourseList getAll(int page, int limit, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<Course> coursePage = courseRepository.findByNameContaining(pageRequest, keyword);

        List<CourseDetail> courseDetailList = coursePage.getContent().stream()
                .map(Mapper::courseMapper)
                .collect(Collectors.toList());

        return CourseList.builder()
                .courses(courseDetailList)
                .totalPages(coursePage.getTotalPages())
                .build();
    }

    @Override
    public CourseDetail getById(Long id) {
        return courseRepository.findById(id)
                .map(Mapper::courseMapper)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id)
                );
    }

    @Override
    @Transactional
    public List<CourseDetail> save(List<CourseDTO> courseDTOList) {
        List<Course> coursesToSave = courseDTOList.stream()
                .map(courseDTO -> {
                    Set<Category> categoriesToSave = convertToCategorySet(courseDTO.categorySet());
                    return Course.builder()
                            .name(courseDTO.name())
                            .price(courseDTO.price())
                            .discount(courseDTO.discount())
                            .thumbnail(courseDTO.thumbnail())
                            .categories(categoriesToSave)
                            .build();
                })
                .collect(Collectors.toList());

        List<Course> savedCourses = courseRepository.saveAll(coursesToSave);

        return savedCourses.stream()
                .map(Mapper::courseMapper)
                .toList();
    }

    @Override
    public CourseDetail update(CourseDTO courseDTO, Long id) {
        Course savedCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found :" + id));
        savedCourse.setName(courseDTO.name());
        savedCourse.setPrice(courseDTO.price());
        savedCourse.setDiscount(courseDTO.discount());
        savedCourse.setThumbnail(courseDTO.thumbnail());
        savedCourse.setCategories(convertToCategorySet(courseDTO.categorySet()));
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        courseRepository.delete(course);
    }


    private Set<Category> convertToCategorySet(Set<CategoryDTO> categoryDTOSet) {
        return categoryDTOSet.stream()
                .map(categoryDTO -> {
                    if (categoryRepository.findByName(categoryDTO.name()).isPresent()) {
                        throw new RuntimeException("duplicate category :" + categoryDTO.name());
                    }
                    Long parentId = categoryDTO.parentCategoryId() == null ? 0L : categoryDTO.parentCategoryId();
                    Category parentCategory = categoryRepository.findById(parentId).orElse(null);
                    return Category.builder()
                            .name(categoryDTO.name())
                            .parentCategory(parentCategory)
                            .build();
                })
                .collect(Collectors.toSet());
    }
}

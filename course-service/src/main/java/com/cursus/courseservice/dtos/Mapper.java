package com.cursus.courseservice.dtos;

import com.cursus.courseservice.dtos.response.category.CategoryDetail;
import com.cursus.courseservice.dtos.response.course.CourseDetail;
import com.cursus.courseservice.entities.Category;
import com.cursus.courseservice.entities.Course;

import java.util.stream.Collectors;

public class Mapper {

    private Mapper() {}

    public static CourseDetail courseMapper(Course course) {
        return CourseDetail.builder()
                .id(course.getId())
                .name(course.getName())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .thumbnail(course.getThumbnail())
                .categories(course.getCategories().stream()
                        .map(Mapper::categoryMapper)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public static CategoryDetail categoryMapper(Category category) {
        return category == null ? null :
                CategoryDetail.builder()
                .id(category.getId())
                .name(category.getName())
                .parentCategoryId(category.getParentCategory() == null ? null : category.getParentCategory().getId())
                .build();
    }

}

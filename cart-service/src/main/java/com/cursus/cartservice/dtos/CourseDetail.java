package com.cursus.cartservice.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class CourseDetail {

    private Long id;

    private String name;

    private double price;

    private double discount;

    private String thumbnail;

    private Set<CategoryDetail> categories;
}

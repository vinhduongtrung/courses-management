package com.cursus.cartservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemRequest {

    private Long courseId;

    private String courseName;

    private Double coursePrice;
}

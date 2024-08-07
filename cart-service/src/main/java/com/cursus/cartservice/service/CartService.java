package com.cursus.cartservice.service;

import com.cursus.cartservice.dtos.CartResponse;

public interface CartService {

    CartResponse viewCart(Long studentId);

    void addCourseToCart(Long courseId, Long studentId);

    void removeCourseFromCart(Long courseId, Long studentId);
}

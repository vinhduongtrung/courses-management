package com.cursus.cartservice.service.impl;

import com.cursus.cartservice.dtos.CartResponse;
import com.cursus.cartservice.dtos.CartItemResponse;
import com.cursus.cartservice.dtos.CourseDetail;
import com.cursus.cartservice.entities.Cart;
import com.cursus.cartservice.entities.CartItem;
import com.cursus.cartservice.config.feignClient.CourseFeignClient;
import com.cursus.cartservice.repository.CartRepository;
import com.cursus.cartservice.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;

    private CourseFeignClient courseFeignClient;

    @Override
    public CartResponse viewCart(Long studentId) {

        Cart cart = cartRepository.findByStudentId(studentId).orElseThrow(() ->
                new RuntimeException("Cart not found : " + studentId));

        return CartResponse.builder()
                .items(cart.getItems().stream()
                        .map(cartItem -> CartItemResponse.builder()
                                .courseId(cartItem.getCourseId())
                                .courseName(cartItem.getCourseName())
                                .coursePrice(cartItem.getCoursePrice())
                                .build())
                        .collect(Collectors.toList()))
                .totalPrice(cart.getItems().stream()
                        .mapToDouble(CartItem::getCoursePrice)
                        .sum())
                .build();
    }

    @Override
    public void addCourseToCart(Long courseId, Long studentId) {
        Cart cart = cartRepository.findByStudentId(studentId).orElseThrow(() ->
                new RuntimeException("Cart not found : " + studentId));

        // Check if any item in cart matches the courseId
        for (CartItem item : cart.getItems()) {
            if (item.getCourseId().equals(courseId)) {
                throw new RuntimeException(
                        "Course with ID " + courseId + " already exist in cart.");
            }
        }

        // If item not exist, call Course Controller to get it
        CourseDetail courseDetail = courseFeignClient.getById(courseId);

        cart.getItems().add(CartItem.builder()
                .courseId(courseDetail.getId())
                .courseName(courseDetail.getName())
                .coursePrice(courseDetail.getPrice())
                .build()
        );

        cartRepository.save(cart);

    }

    @Override
    public void removeCourseFromCart(Long courseId, Long studentId) {
        Cart cart = cartRepository.findByStudentId(studentId).orElseThrow(() ->
                        new RuntimeException("Cart not found : " + studentId));

        // Check if any item in cart matches the courseId
        boolean itemFound = false;
        Iterator<CartItem> iterator = cart.getItems().iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getCourseId().equals(courseId)) {
                iterator.remove();
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            throw new RuntimeException("Course with ID " + courseId + " not found in cart.");
        }

        cartRepository.save(cart);
    }
}

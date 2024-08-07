package com.cursus.cartservice.controller;


import com.cursus.cartservice.dtos.CartResponse;
import com.cursus.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> viewCart(@PathVariable("id") Long studentId) {
        return ResponseEntity.ok().body(cartService.viewCart(studentId));
    }

    @PostMapping("/{id}/course/{courseId}")
    public ResponseEntity<?> addCourseToCart(@PathVariable Long courseId,
                                             @PathVariable("id") Long studentId) {
        cartService.addCourseToCart(courseId, studentId);
        return ResponseEntity.ok().body("Add course successfully");
    }


    @DeleteMapping("/{id}/course/{courseId}")
    public ResponseEntity<?> removeCourseFromCart(@PathVariable Long courseId,
                                     @PathVariable("id") Long studentId) {
        cartService.removeCourseFromCart(courseId, studentId);
        return ResponseEntity.ok().body("Remove course successfully");
    }
}

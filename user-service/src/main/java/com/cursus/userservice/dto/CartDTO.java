package com.cursus.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private String studentId;

    private List<CartItemDTO> cartItems;

    private Double totalPay;

    private String status;
}

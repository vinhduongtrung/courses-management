package com.cursus.paymentservice.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartDTO {
    private String studentId;

    private List<CartItemDTO> cartItems;

    private Double totalPay;

    private String status;
}

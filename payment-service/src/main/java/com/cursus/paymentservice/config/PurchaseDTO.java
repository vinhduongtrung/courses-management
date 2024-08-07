package com.cursus.paymentservice.config;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseDTO implements Serializable {
    private Long studentId;
    private Long courseId;
    private String status;
}

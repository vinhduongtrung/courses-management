package com.cursus.cartservice.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="carts")
public class Cart {

    // Because one student have only one cart
    // So student id is also cart id
    @Id
    @Column(name="id", unique = true)
    private Long studentId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private Set<CartItem> items = new HashSet<>();
}

package com.cursus.userservice.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Instructor extends User{

    public Instructor(Long id, String name, String email, String password, Instant createdDate, Role role) {
        super(id, name, email, password, createdDate, role);
    }
}

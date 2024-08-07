package com.cursus.userservice.service.impl;

import com.cursus.userservice.dto.LoginRequest;
import com.cursus.userservice.entity.User;
import com.cursus.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public void login(LoginRequest loginRequest) {
        System.out.println("Logging in " + loginRequest.email());
    }

    @Override
    public void logout(User user) {
        System.out.println("Logging out");
    }

    @Override
    public void register(User user) {

    }

    @Override
    public void viewHomepage(User user) {
        System.out.println("View home page");
    }
}

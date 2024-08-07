package com.cursus.userservice.service;

import com.cursus.userservice.dto.LoginRequest;
import com.cursus.userservice.entity.User;

public interface UserService {
    void login(LoginRequest loginRequest);
    void logout(User user);
    void register(User user);
    void viewHomepage(User user);
}

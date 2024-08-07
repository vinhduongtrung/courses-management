package com.cursus.userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface JwtService {
    long getRefreshTokenExpiration();
    String generateAccessToken(String email);
    String generateRefreshToken();
    String generateForgotPasswordToken(String email);
    String getEmail(String jwt);
    void checkExpirationDate(String jwt);
    Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException;
    String getHEADER_STRING();
}

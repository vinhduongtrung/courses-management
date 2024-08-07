package com.cursus.userservice.config.security;

import com.cursus.userservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;


@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Bypass public uri
        if (Arrays.asList(
                        "/",
                        "/api/v1/login",
                        "/api/v1/logout",
                        "/api/v1/extendToken",
                        "/api/v1/forgot-password",
                        "/api/v1/reset-password",
                        "/api/v1/check-token",
                        "/api/v1/login-by-google",
                        "/ws/**")
                .contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().startsWith("/ws/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bypass swagger
        if (Arrays.asList(
                        "/swagger-ui",
                        "/swagger-ui/index.html",
                        "/v3/api-docs",
                        "/swagger-ui/favicon-32x32.png",
                        "/v3/api-docs/swagger-config",
                        "/swagger-ui/swagger-ui.css",
                        "/swagger-ui/swagger-initializer.js",
                        "/swagger-ui/swagger-ui-standalone-preset.js",
                        "/swagger-ui/index.css",
                        "/swagger-ui/swagger-ui-bundle.js")
                .contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check if token is existed
        Authentication authentication = jwtService.getAuthentication(request, response);
        if (authentication == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}

package com.cursus.userservice.config.security;

import com.cursus.userservice.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * Restrict access to specific urls
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF protection
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS protection
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Configure authorization rules for HTTP requests
                .authorizeHttpRequests(authorize ->
                        authorize
                                // Permit access to public URIs specified by PUBLIC_URI
                                .requestMatchers(
                                        "/",
                                        "/api/v1/login",
                                        "/api/v1/logout",
                                        "/api/v1/extendToken",
                                        "/api/v1/forgot-password",
                                        "/api/v1/reset-password",
                                        "/api/v1/check-token",
                                        "/api/v1/login-by-google",
                                        "/ws/**"
                                ).permitAll()

                                // Permit all access to swagger
                                .requestMatchers(
                                        "/swagger-ui",
                                        "/swagger-ui/index.html",
                                        "/v3/api-docs",
                                        "/swagger-ui/favicon-32x32.png",
                                        "/v3/api-docs/swagger-config",
                                        "/swagger-ui/swagger-ui.css",
                                        "/swagger-ui/swagger-initializer.js",
                                        "/swagger-ui/swagger-ui-standalone-preset.js",
                                        "/swagger-ui/index.css",
                                        "/swagger-ui/swagger-ui-bundle.js"
                                ).permitAll()

                                // Permit access to all roles
                                .requestMatchers(
                                        "/api/v1/login",
                                        "/api/v1/logout",
                                        "/api/v1/dashboard"
                                ).hasAnyAuthority(Role.GUEST.name(), Role.STUDENT.name(), Role.INSTRUCTOR.name(), Role.ADMIN.name())
                                // Permit access to student, instructor

                                .requestMatchers(
                                        "/api/v1/register",
                                        "/api/v1/students/homepage",
                                        "/api/v1/courses/enrolled-courses",
                                        "/api/v1/students/total-courses",
                                        "/api/v1/profile-analytics",
                                        "/api/v1/user/profile",
                                        "/api/v1/user/password"

                                ).hasAnyAuthority(Role.GUEST.name())

                                .requestMatchers(
                                        "/api/v1/students/purchase-courses",
                                        "/api/v1/students/*/enroll-course/*"

                                ).hasAnyAuthority(Role.STUDENT.name())

//                                // Permit access to admin, teacher
//                                .requestMatchers(TEACHER_RESTRICTION).hasAnyAuthority(TEACHER.name(), ADMIN.name())
//
//                                // Permit access to admin only
//                                .requestMatchers(ADMIN_RESTRICTION).hasAuthority(ADMIN.name())

                                // Authenticate any other requests
//                                .anyRequest().authenticated()
                                .anyRequest().denyAll()
                )

                // Set custom authentication provider
                .authenticationProvider(authenticationProvider)

                // Add custom authentication filter to filter chain
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Configure exception handling for unauthorized and access denied cases
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .build();
    }
}


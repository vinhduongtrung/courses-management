package com.cursus.userservice.service.impl;

import com.cursus.userservice.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final UserDetailsService userDetailsService;
    private final String HEADER_STRING = "Authorization";
    private final String SECRET = "404E635266556A5435345538782F413F4428472B4B62506765765A4324";
    private int ACCESS_TOKEN_EXPIRATION = 3600;

    private int FORGOT_PASSWORD_TOKEN_EXPIRATION = 900;

    @Override
    public long getRefreshTokenExpiration() {
        int REFRESH_TOKEN_EXPIRATION = 604800;
        return REFRESH_TOKEN_EXPIRATION;
    }

    @Override
    public String generateAccessToken(String email) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        c.add(Calendar.SECOND, ACCESS_TOKEN_EXPIRATION);
        Date currentDateInHoChiMinh = c.getTime();
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(currentDateInHoChiMinh)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generateForgotPasswordToken(String email) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        c.add(Calendar.SECOND, FORGOT_PASSWORD_TOKEN_EXPIRATION);
        Date currentDateInHoChiMinh = c.getTime();
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(currentDateInHoChiMinh)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    @Override
    public String getEmail(String jwt) {
        String email;
        try {
            email = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Invalid token");
        }
        return email;
    }

    @Override
    public void checkExpirationDate(String jwt) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getExpiration();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("{\"statusCode\":\"400\",\"errorMessage\":\"accessToken had expired\"}");
        }
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jwt = request.getHeader(HEADER_STRING);
        Authentication authentication = null;
        try {
            if (jwt != null) {
                // Extract email from token
                String email = getEmail(jwt);

                if (email != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(400);
            response.getWriter().write("{\"statusCode\":\"400\",\"errorMessage\":\"accessToken had expired\"}");
        }
        return authentication;
    }

    @Override
    public String getHEADER_STRING() {
        return HEADER_STRING;
    }
}

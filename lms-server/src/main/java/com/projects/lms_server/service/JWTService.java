package com.projects.lms_server.service;

import com.projects.lms_server.entites.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Slf4j
public class JWTService {
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {

        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserEntity user) {
        return Jwts.builder()
                .subject(user.getUserId())
                .claim("role",user.getRole())
                .claim("email",user.getEmail())
                .claim("userId",user.getUserId())
                .claim("userName",user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60*15))
                .signWith(getSecretKey())
                .compact();

    }

    public String generateRefreshToken(UserEntity user) {
        return Jwts.builder()
                .subject(user.getUserId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60*24*15L))
                .signWith(getSecretKey())
                .compact();
    }

    public String getUserIdFromToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

}

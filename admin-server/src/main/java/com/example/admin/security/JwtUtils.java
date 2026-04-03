package com.example.admin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${admin.jwt.secret}")
    private String secret;

    @Value("${admin.jwt.expire-seconds:7200}")
    private long expireSeconds;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expireSeconds * 1000);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(exp)
                .signWith(key())
                .compact();
    }

    public Long parseUserId(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        return Long.valueOf(claims.getSubject());
    }
}

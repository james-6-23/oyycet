package com.cet.practice.security;

import com.cet.practice.entity.CetUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expires-seconds}")
    private long expiresSeconds;

    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "JWT secret 未配置！请设置环境变量 APP_JWT_SECRET（至少 32 字节）");
        }
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                    "JWT secret 太短（需要 >= 32 字节用于 HS256）");
        }
        log.info("JWT 密钥校验通过，Token 有效期: {} 秒", expiresSeconds);
    }

    public String generateToken(CetUser user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("user is null");
        }

        Date now = new Date();
        Date exp = new Date(now.getTime() + expiresSeconds * 1000L);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        String sub = claims.getSubject();
        return sub == null ? null : Long.valueOf(sub);
    }

    public String getRole(String token) {
        Claims claims = parseClaims(token);
        String role = claims.get("role", String.class);
        return role == null ? "STUDENT" : role;
    }

    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getKey() {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT secret too short (need >= 32 bytes for HS256)");
        }
        return Keys.hmacShaKeyFor(bytes);
    }
}


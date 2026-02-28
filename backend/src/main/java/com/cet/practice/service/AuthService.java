package com.cet.practice.service;

import com.cet.practice.common.BusinessException;
import com.cet.practice.common.enums.UserRole;
import com.cet.practice.converter.UserConverter;
import com.cet.practice.dto.*;
import com.cet.practice.entity.CetUser;
import com.cet.practice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final CetUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CacheService cacheService;

    /** 同一用户名最多连续失败次数 */
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    /** 锁定时长（分钟） */
    private static final int LOCK_MINUTES = 15;
    private static final String LOGIN_ATTEMPT_KEY_PREFIX = "login:attempt:";

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        String username = req.getUsername().trim();
        if (userService.getByUsername(username) != null) {
            throw new BusinessException(400, "用户名已存在");
        }

        CetUser user = new CetUser();
        user.setUsername(username);
        user.setEmail(req.getEmail());
        user.setNickname(req.getNickname());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.STUDENT.getValue());
        user.setStatus(1);
        userService.save(user);

        AuthResponse resp = new AuthResponse();
        resp.setUser(UserConverter.toUserDTO(user));
        resp.setToken(jwtTokenProvider.generateToken(user));
        return resp;
    }

    public AuthResponse login(LoginRequest req) {
        String username = req.getUsername();
        String attemptKey = LOGIN_ATTEMPT_KEY_PREFIX + username;

        // 检查是否被锁定
        checkLoginLocked(attemptKey, username);

        CetUser user = userService.getByUsername(username);
        if (user == null) {
            recordFailedAttempt(attemptKey, username);
            throw new BusinessException(400, "用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(403, "账号已禁用");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            recordFailedAttempt(attemptKey, username);
            throw new BusinessException(400, "用户名或密码错误");
        }

        // 登录成功，清除失败计数
        cacheService.delete(attemptKey);

        AuthResponse resp = new AuthResponse();
        resp.setUser(UserConverter.toUserDTO(user));
        resp.setToken(jwtTokenProvider.generateToken(user));
        return resp;
    }

    public UserDTO me(Long userId) {
        CetUser user = userService.getById(userId);
        if (user == null || (user.getDeleted() != null && user.getDeleted() == 1)) {
            throw new BusinessException(401, "用户不存在或已删除");
        }
        return UserConverter.toUserDTO(user);
    }

    private void checkLoginLocked(String attemptKey, String username) {
        Long attempts = cacheService.get(attemptKey, Long.class);
        if (attempts != null && attempts >= MAX_LOGIN_ATTEMPTS) {
            Long ttl = cacheService.getExpire(attemptKey);
            long remainMin = (ttl != null && ttl > 0) ? (ttl / 60 + 1) : LOCK_MINUTES;
            log.warn("用户 {} 登录失败次数过多，已锁定", username);
            throw new BusinessException(429, "登录失败次数过多，请 " + remainMin + " 分钟后再试");
        }
    }

    private void recordFailedAttempt(String attemptKey, String username) {
        Long count = cacheService.increment(attemptKey, Duration.ofMinutes(LOCK_MINUTES));
        long remaining = MAX_LOGIN_ATTEMPTS - (count != null ? count : 0);
        if (remaining > 0) {
            log.info("用户 {} 登录失败，剩余尝试次数: {}", username, remaining);
        } else {
            log.warn("用户 {} 登录失败次数达到上限，已锁定 {} 分钟", username, LOCK_MINUTES);
        }
    }

}

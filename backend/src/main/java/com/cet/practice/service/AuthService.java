package com.cet.practice.service;

import com.cet.practice.common.BusinessException;
import com.cet.practice.dto.*;
import com.cet.practice.entity.CetUser;
import com.cet.practice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CetUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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
        user.setRole("STUDENT");
        user.setStatus(1);
        userService.save(user);

        AuthResponse resp = new AuthResponse();
        resp.setUser(toUserDTO(user));
        resp.setToken(jwtTokenProvider.generateToken(user));
        return resp;
    }

    public AuthResponse login(LoginRequest req) {
        CetUser user = userService.getByUsername(req.getUsername());
        if (user == null) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(403, "账号已禁用");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        AuthResponse resp = new AuthResponse();
        resp.setUser(toUserDTO(user));
        resp.setToken(jwtTokenProvider.generateToken(user));
        return resp;
    }

    public UserDTO me(Long userId) {
        CetUser user = userService.getById(userId);
        if (user == null || (user.getDeleted() != null && user.getDeleted() == 1)) {
            throw new BusinessException(401, "用户不存在或已删除");
        }
        return toUserDTO(user);
    }

    private static UserDTO toUserDTO(CetUser user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setRole(user.getRole());
        return dto;
    }
}


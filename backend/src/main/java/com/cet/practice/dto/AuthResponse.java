package com.cet.practice.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private UserDTO user;
}


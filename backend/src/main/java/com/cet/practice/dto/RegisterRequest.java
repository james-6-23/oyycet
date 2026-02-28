package com.cet.practice.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度需在 3-50 之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度需在 6-100 之间")
    private String password;

    @Email(message = "邮箱格式不正确")
    @Size(max = 255, message = "邮箱长度不能超过 255")
    private String email;

    @Size(max = 50, message = "昵称长度不能超过 50")
    private String nickname;
}

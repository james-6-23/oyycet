package com.cet.practice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminUserDTO {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String role;
    private Integer status;
    private LocalDateTime createTime;
}

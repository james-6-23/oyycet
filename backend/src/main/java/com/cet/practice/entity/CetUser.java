package com.cet.practice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cet_user")
public class CetUser extends BaseEntity {

    private String username;
    private String email;
    private String passwordHash;
    private String nickname;
    private String role;   // STUDENT/ADMIN
    private Integer status; // 1 enabled, 0 disabled
}


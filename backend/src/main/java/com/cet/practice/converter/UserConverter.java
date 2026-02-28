package com.cet.practice.converter;

import com.cet.practice.dto.UserDTO;
import com.cet.practice.entity.CetUser;

public final class UserConverter {

    private UserConverter() {
    }

    public static UserDTO toUserDTO(CetUser user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setRole(user.getRole());
        return dto;
    }
}

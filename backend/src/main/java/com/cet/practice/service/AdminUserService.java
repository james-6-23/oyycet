package com.cet.practice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cet.practice.common.BusinessException;
import com.cet.practice.dto.AdminUserDTO;
import com.cet.practice.entity.CetUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final CetUserService userService;

    public Page<AdminUserDTO> pageUsers(int current, int size, String username) {
        LambdaQueryWrapper<CetUser> wrapper = new LambdaQueryWrapper<CetUser>()
                .eq(CetUser::getDeleted, 0);

        if (username != null && !username.trim().isEmpty()) {
            wrapper.like(CetUser::getUsername, username.trim());
        }

        wrapper.orderByDesc(CetUser::getCreateTime);

        Page<CetUser> page = userService.page(new Page<>(current, size), wrapper);

        Page<AdminUserDTO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(page.getRecords().stream().map(this::toAdminUserDTO).toList());
        return result;
    }

    public void updateRole(Long userId, Long operatorId, String role) {
        if (userId.equals(operatorId)) {
            throw new BusinessException(400, "不能修改自己的角色");
        }
        CetUser user = userService.getById(userId);
        if (user == null || (user.getDeleted() != null && user.getDeleted() == 1)) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setRole(role);
        userService.updateById(user);
    }

    public void updateStatus(Long userId, Long operatorId, Integer status) {
        if (userId.equals(operatorId)) {
            throw new BusinessException(400, "不能修改自己的状态");
        }
        CetUser user = userService.getById(userId);
        if (user == null || (user.getDeleted() != null && user.getDeleted() == 1)) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setStatus(status);
        userService.updateById(user);
    }

    private AdminUserDTO toAdminUserDTO(CetUser user) {
        AdminUserDTO dto = new AdminUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreateTime(user.getCreateTime());
        return dto;
    }
}

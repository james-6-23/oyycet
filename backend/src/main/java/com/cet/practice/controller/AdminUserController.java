package com.cet.practice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cet.practice.common.Result;
import com.cet.practice.dto.AdminUserDTO;
import com.cet.practice.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public Result<Page<AdminUserDTO>> list(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username) {
        return Result.success(adminUserService.pageUsers(current, size, username));
    }

    @PutMapping("/{id}/role")
    public Result<Void> updateRole(@PathVariable Long id,
                                   @RequestBody Map<String, String> body,
                                   @RequestAttribute("userId") Long operatorId) {
        String role = body.get("role");
        adminUserService.updateRole(id, operatorId, role);
        return Result.success(null);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id,
                                     @RequestBody Map<String, Integer> body,
                                     @RequestAttribute("userId") Long operatorId) {
        Integer status = body.get("status");
        adminUserService.updateStatus(id, operatorId, status);
        return Result.success(null);
    }
}

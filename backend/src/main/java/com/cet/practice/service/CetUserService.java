package com.cet.practice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cet.practice.entity.CetUser;
import com.cet.practice.mapper.CetUserMapper;
import org.springframework.stereotype.Service;

@Service
public class CetUserService extends ServiceImpl<CetUserMapper, CetUser> {

    public CetUser getByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return getOne(new LambdaQueryWrapper<CetUser>()
                .eq(CetUser::getUsername, username)
                .eq(CetUser::getDeleted, 0)
                .last("LIMIT 1"));
    }
}


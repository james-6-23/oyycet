package com.cet.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cet.practice.entity.CetUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CetUserMapper extends BaseMapper<CetUser> {
}


package com.cet.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cet.practice.entity.CetPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CetPaperMapper extends BaseMapper<CetPaper> {

    @Update("UPDATE cet_paper SET attempts = attempts + 1 WHERE id = #{paperId} AND deleted = 0")
    int incrementAttempts(@Param("paperId") Long paperId);
}


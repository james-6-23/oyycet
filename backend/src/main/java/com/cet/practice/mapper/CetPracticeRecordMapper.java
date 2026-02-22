package com.cet.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cet.practice.entity.CetPracticeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface CetPracticeRecordMapper extends BaseMapper<CetPracticeRecord> {

    @Select("SELECT " +
            "COUNT(*) AS total_records, " +
            "AVG(score) AS avg_score, " +
            "SUM(correct_count) AS total_correct, " +
            "SUM(total_count) AS total_questions, " +
            "SUM(duration_seconds) AS total_duration " +
            "FROM cet_practice_record WHERE user_id = #{userId} AND deleted = 0")
    Map<String, Object> getUserStats(@Param("userId") Long userId);
}


package com.cet.practice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cet_practice_record")
public class CetPracticeRecord extends BaseEntity {

    private Long userId;
    private Long paperId;
    private Integer score;
    private Integer durationSeconds;
    private Integer correctCount;
    private Integer totalCount;
    private String answerDetailJson;
    private LocalDateTime finishTime;
}


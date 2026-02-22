package com.cet.practice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PracticeRecordDTO {
    private Long id;
    private Long paperId;
    private String paperTitle;
    private Integer paperYear;
    private String paperType;
    private Integer score;
    private Integer durationSeconds;
    private Integer correctCount;
    private Integer totalCount;
    private LocalDateTime finishTime;
}


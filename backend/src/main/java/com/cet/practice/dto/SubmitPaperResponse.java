package com.cet.practice.dto;

import lombok.Data;

@Data
public class SubmitPaperResponse {
    private Long recordId;
    private Integer score;
    private Integer correctCount;
    private Integer totalCount;
    private Integer durationSeconds;
}


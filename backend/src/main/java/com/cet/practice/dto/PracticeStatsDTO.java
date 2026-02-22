package com.cet.practice.dto;

import lombok.Data;

@Data
public class PracticeStatsDTO {
    private Long totalPractices;
    private Long averageScore;
    private Long totalCorrect;
    private Long totalQuestions;
    private Long accuracy;
    private Long totalDuration;
}


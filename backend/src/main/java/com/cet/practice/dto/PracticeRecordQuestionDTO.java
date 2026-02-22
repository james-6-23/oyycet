package com.cet.practice.dto;

import lombok.Data;

@Data
public class PracticeRecordQuestionDTO {
    private Long questionId;
    private Integer questionNo;
    private String type;
    private String userAnswer;
    private String correctAnswer;
    private Boolean isCorrect;
    private String content;
    private String optionsJson;
    private String explanation;
    private String passage;
}


package com.cet.practice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private Long paperId;
    private String type;
    private String subType;
    private Integer questionNumber;
    private BigDecimal score;
    private String content;
    private List<OptionDTO> options;
    private String correctAnswer;
    private String explanation;
    private String passage;
    private String wordBank;
    private String audioUrl;
    private Integer audioStartTime;
    private Integer audioEndTime;
    private String passageGroup;
    private String directions;
    private Integer sortOrder;
}


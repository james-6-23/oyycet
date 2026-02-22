package com.cet.practice.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Data
public class QuestionImportDTO {
    @NotBlank
    private String type;
    private String subType;
    private Integer questionNo;
    private BigDecimal score;
    private String content;
    private List<OptionDTO> options;
    private String correctAnswer;
    private String explanation;
    private String passage;
    private Object wordBank;
    private String audioUrl;
    private Integer audioStartTime;
    private Integer audioEndTime;
    private String passageGroup;
    private String directions;
    private Integer sortOrder;
}

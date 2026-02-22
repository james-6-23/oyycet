package com.cet.practice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cet_paper_question")
public class CetPaperQuestion extends BaseEntity {

    private Long paperId;
    private String type;
    private String subType;
    private Integer questionNo;
    private BigDecimal score;
    private String content;
    private String optionsJson;
    private String correctAnswer;
    private String explanation;
    private String passage;
    private String wordBankJson;
    private String audioUrl;
    private Integer audioStartTime;
    private Integer audioEndTime;
    private String passageGroup;
    private String directions;
    private Integer sortOrder;
}


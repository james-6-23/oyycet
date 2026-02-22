package com.cet.practice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cet_paper")
public class CetPaper extends BaseEntity {

    private String title;
    private Integer year;
    private Integer month;
    private Integer paperNo;
    private String difficulty;
    private String type;
    private Integer durationMin;
    private String status;
    private Integer attempts;
    private Long listeningRefPaperId;
    private Integer listeningRefPaperNo;
}


package com.cet.practice.dto;

import lombok.Data;

@Data
public class PaperDTO {
    private Long id;
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


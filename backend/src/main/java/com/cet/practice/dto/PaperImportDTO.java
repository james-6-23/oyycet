package com.cet.practice.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class PaperImportDTO {
    @NotBlank
    private String title;
    private Integer year;
    private Integer month;
    private Integer paperNo;
    private String difficulty;
    private String type;
    private Integer durationMin;
    private Integer listeningRefPaperNo;
}

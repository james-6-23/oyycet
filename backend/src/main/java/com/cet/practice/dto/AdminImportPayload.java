package com.cet.practice.dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class AdminImportPayload {
    @NotNull
    @Valid
    private PaperImportDTO paper;

    @NotNull
    @Valid
    private List<QuestionImportDTO> questions;
}

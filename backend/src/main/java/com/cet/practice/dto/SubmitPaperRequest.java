package com.cet.practice.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
public class SubmitPaperRequest {
    private Integer durationSeconds;

    @NotNull
    private Map<String, String> answers;
}

package com.cet.practice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI 响应结果")
public class AiResponse {

    @Schema(description = "AI 生成的内容")
    private String content;

    @Schema(description = "使用的模型")
    private String model;

    @Schema(description = "Token 使用量")
    private Long tokenUsage;
}

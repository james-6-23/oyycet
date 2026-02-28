package com.cet.practice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * AI 请求 DTO
 */
@Data
@Schema(description = "AI 请求参数")
public class AiRequest {

    @NotBlank(message = "内容不能为空")
    @Size(max = 5000, message = "内容长度不能超过 5000 字符")
    @Schema(description = "请求内容", example = "请翻译这段话")
    private String content;

    @Schema(description = "目标语言 (zh/en)，用于翻译", example = "zh")
    private String targetLanguage;

    @Schema(description = "请求类型 (translate/grammar/writing/chat)", example = "chat")
    private String type;
}

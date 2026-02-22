package com.cet.practice.controller;

import com.cet.practice.common.Result;
import com.cet.practice.dto.AiRequest;
import com.cet.practice.dto.AiResponse;
import com.cet.practice.service.CetAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * AI Controller - CET-4 AI 辅助学习
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI 辅助", description = "CET-4 AI 辅助学习接口")
public class AiController {

    private final CetAiService aiService;

    @PostMapping("/translate")
    @Operation(summary = "翻译", description = "中英互译，targetLanguage: zh(翻译成中文) / en(翻译成英文)")
    public Result<AiResponse> translate(@Valid @RequestBody AiRequest request) {
        String targetLang = request.getTargetLanguage();
        if (targetLang == null || targetLang.isBlank()) {
            targetLang = "zh"; // 默认翻译成中文
        }
        AiResponse response = aiService.translate(request.getContent(), targetLang);
        return Result.success(response);
    }

    @PostMapping("/explain-grammar")
    @Operation(summary = "语法解释", description = "解析英语句子的语法结构")
    public Result<AiResponse> explainGrammar(@Valid @RequestBody AiRequest request) {
        AiResponse response = aiService.explainGrammar(request.getContent());
        return Result.success(response);
    }

    @PostMapping("/score-writing")
    @Operation(summary = "作文评分", description = "对 CET-4 作文进行评分和点评")
    public Result<AiResponse> scoreWriting(@Valid @RequestBody AiRequest request) {
        AiResponse response = aiService.scoreWriting(request.getContent());
        return Result.success(response);
    }

    @PostMapping("/chat")
    @Operation(summary = "AI 对话", description = "与 CET-4 学习助手进行对话")
    public Result<AiResponse> chat(@Valid @RequestBody AiRequest request) {
        AiResponse response = aiService.chat(request.getContent());
        return Result.success(response);
    }
}

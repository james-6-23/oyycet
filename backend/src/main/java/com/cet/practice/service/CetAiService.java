package com.cet.practice.service;

import com.cet.practice.common.BusinessException;
import com.cet.practice.dto.AiResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * CET AI 服务 - 使用 Spring AI 框架
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CetAiService {

    private final ChatClient.Builder chatClientBuilder;

    /** 复用 ChatClient 实例，避免每次请求都创建 */
    private ChatClient chatClient;

    /** 检测 prompt 注入的常见模式 */
    private static final Pattern INJECTION_PATTERN = Pattern.compile(
            "(?i)(ignore\\s+(previous|above|all)\\s+(instructions|prompts|rules))" +
            "|(you\\s+are\\s+now\\s+)" +
            "|(system\\s*:\\s*)" +
            "|(\\bDAN\\b.*\\bmode\\b)" +
            "|(jailbreak)",
            Pattern.CASE_INSENSITIVE
    );

    @PostConstruct
    public void init() {
        this.chatClient = chatClientBuilder.build();
        log.info("ChatClient 初始化完成");
    }

    /**
     * 翻译文本
     */
    public AiResponse translate(String content, String targetLanguage) {
        String langName = "zh".equalsIgnoreCase(targetLanguage) ? "中文" : "英文";
        String systemPrompt = String.format(
                "你是一个专业的英语翻译助手。请将用户输入的内容翻译成%s。只返回翻译结果，不要添加任何解释。",
                langName);
        return doChat(systemPrompt, content);
    }

    /**
     * 解释语法
     */
    public AiResponse explainGrammar(String sentence) {
        String systemPrompt = """
                你是一个专业的英语语法老师。请分析用户输入的英语句子，包括：
                1. 句子结构（主谓宾等）
                2. 涉及的语法点
                3. 关键词汇解释
                4. 中文翻译
                请用简洁清晰的方式解释，适合 CET-4 考生理解。
                """;
        return doChat(systemPrompt, sentence);
    }

    /**
     * 作文评分
     */
    public AiResponse scoreWriting(String essay) {
        String systemPrompt = """
                你是一个专业的 CET-4 作文评分老师。请对用户的作文进行评分和点评：
                1. 给出总分（满分 15 分）
                2. 评价优点
                3. 指出不足
                4. 给出具体的改进建议
                5. 提供一个改进后的范例段落
                请用鼓励性的语气，帮助学生进步。
                """;
        return doChat(systemPrompt, essay);
    }

    /**
     * 通用对话
     */
    public AiResponse chat(String userMessage) {
        String systemPrompt = """
                你是一个专业的 CET-4 英语学习助手。你可以帮助用户：
                - 解答英语学习问题
                - 解释语法和词汇
                - 提供学习建议
                - 进行英语对话练习
                请用友好、专业的语气回答问题。
                重要：你只回答与英语学习相关的问题，拒绝回答无关话题。
                """;
        return doChat(systemPrompt, userMessage);
    }

    /**
     * 核心对话方法（含输入防护）
     */
    private AiResponse doChat(String systemPrompt, String userMessage) {
        // 输入防护：检测 prompt 注入
        if (INJECTION_PATTERN.matcher(userMessage).find()) {
            log.warn("检测到疑似 prompt 注入: {}", userMessage.substring(0, Math.min(100, userMessage.length())));
            throw new BusinessException(400, "输入内容包含不允许的指令");
        }

        try {
            Prompt prompt = new Prompt(List.of(
                    new SystemMessage(systemPrompt),
                    new UserMessage(userMessage)));

            ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

            String content = response.getResult().getOutput().getText();
            String model = response.getMetadata().getModel();
            Long tokenUsage = response.getMetadata().getUsage() != null
                    ? Long.valueOf(response.getMetadata().getUsage().getTotalTokens())
                    : null;

            return AiResponse.builder()
                    .content(content)
                    .model(model)
                    .tokenUsage(tokenUsage)
                    .build();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI 调用失败: {}", e.getMessage(), e);
            return AiResponse.builder()
                    .content("抱歉，AI 服务暂时不可用，请稍后再试。")
                    .build();
        }
    }
}

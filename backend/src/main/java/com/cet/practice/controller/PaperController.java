package com.cet.practice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cet.practice.common.BusinessException;
import com.cet.practice.common.Result;
import com.cet.practice.dto.*;
import com.cet.practice.entity.CetPaper;
import com.cet.practice.entity.CetPaperQuestion;
import com.cet.practice.service.CetPaperQuestionService;
import com.cet.practice.service.CetPaperService;
import com.cet.practice.service.CetPracticeRecordService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/papers")
@RequiredArgsConstructor
public class PaperController {

    private final CetPaperService paperService;
    private final CetPaperQuestionService questionService;
    private final CetPracticeRecordService recordService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public Result<Page<PaperDTO>> pagePapers(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer paperNo,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String difficulty) {
        Page<CetPaper> page = paperService.pagePublished(current, size, year, month, paperNo, type, difficulty);
        Page<PaperDTO> dtoPage = new Page<PaperDTO>(page.getCurrent(), page.getSize(), page.getTotal());
        List<PaperDTO> list = new ArrayList<PaperDTO>();
        for (CetPaper p : page.getRecords()) {
            list.add(toPaperDTO(p));
        }
        dtoPage.setRecords(list);
        return Result.success(dtoPage);
    }

    @GetMapping("/{id}")
    public Result<PaperDTO> getPaper(@PathVariable Long id) {
        CetPaper p = paperService.getById(id);
        if (p == null || (p.getDeleted() != null && p.getDeleted() == 1) || !"PUBLISHED".equalsIgnoreCase(p.getStatus())) {
            return Result.notFound("试卷不存在");
        }
        return Result.success(toPaperDTO(p));
    }

    @GetMapping("/{id}/questions")
    public Result<List<QuestionDTO>> getQuestions(@PathVariable Long id, @RequestParam(defaultValue = "practice") String mode) {
        CetPaper p = paperService.getById(id);
        if (p == null || (p.getDeleted() != null && p.getDeleted() == 1) || !"PUBLISHED".equalsIgnoreCase(p.getStatus())) {
            return Result.notFound("试卷不存在");
        }

        List<CetPaperQuestion> questions = questionService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<CetPaperQuestion>()
                        .eq(CetPaperQuestion::getPaperId, id)
                        .eq(CetPaperQuestion::getDeleted, 0)
                        .orderByAsc(CetPaperQuestion::getSortOrder)
                        .orderByAsc(CetPaperQuestion::getId)
        );

        boolean hideAnswer = "exam".equalsIgnoreCase(mode);
        List<QuestionDTO> dtos = new ArrayList<QuestionDTO>();
        for (CetPaperQuestion q : questions) {
            QuestionDTO dto = toQuestionDTO(q);
            if (hideAnswer) {
                dto.setCorrectAnswer(null);
                dto.setExplanation(null);
            }
            dtos.add(dto);
        }
        return Result.success(dtos);
    }

    @PostMapping("/{id}/submit")
    public Result<SubmitPaperResponse> submit(@PathVariable Long id,
                                              @Valid @RequestBody SubmitPaperRequest req,
                                              @RequestAttribute("userId") Long userId) {
        if (req.getAnswers() == null) {
            throw new BusinessException(400, "answers 不能为空");
        }

        Map<Long, String> answers = new HashMap<Long, String>();
        for (Map.Entry<String, String> e : req.getAnswers().entrySet()) {
            try {
                answers.put(Long.valueOf(e.getKey()), e.getValue());
            } catch (Exception ignore) {
                // ignore invalid key
            }
        }

        SubmitPaperResponse resp = recordService.submit(userId, id, answers, req.getDurationSeconds());
        return Result.success(resp);
    }

    private static PaperDTO toPaperDTO(CetPaper p) {
        PaperDTO dto = new PaperDTO();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setYear(p.getYear());
        dto.setMonth(p.getMonth());
        dto.setPaperNo(p.getPaperNo());
        dto.setDifficulty(p.getDifficulty());
        dto.setType(p.getType());
        dto.setDurationMin(p.getDurationMin());
        dto.setStatus(p.getStatus());
        dto.setAttempts(p.getAttempts());
        dto.setListeningRefPaperId(p.getListeningRefPaperId());
        dto.setListeningRefPaperNo(p.getListeningRefPaperNo());
        return dto;
    }

    private QuestionDTO toQuestionDTO(CetPaperQuestion q) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(q.getId());
        dto.setPaperId(q.getPaperId());
        dto.setType(q.getType());
        dto.setSubType(q.getSubType());
        dto.setQuestionNumber(q.getQuestionNo());
        dto.setScore(q.getScore());
        dto.setContent(q.getContent());
        dto.setCorrectAnswer(q.getCorrectAnswer());
        dto.setExplanation(q.getExplanation());
        dto.setPassage(q.getPassage());
        dto.setWordBank(q.getWordBankJson());
        dto.setAudioUrl(q.getAudioUrl());
        dto.setAudioStartTime(q.getAudioStartTime());
        dto.setAudioEndTime(q.getAudioEndTime());
        dto.setPassageGroup(q.getPassageGroup());
        dto.setDirections(q.getDirections());
        dto.setSortOrder(q.getSortOrder());

        dto.setOptions(parseOptions(q.getOptionsJson()));
        return dto;
    }

    private List<OptionDTO> parseOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            List<Map<String, Object>> arr = objectMapper.readValue(optionsJson, new TypeReference<List<Map<String, Object>>>() {});
            List<OptionDTO> list = new ArrayList<OptionDTO>();
            for (Map<String, Object> item : arr) {
                Object key = item.get("key");
                Object content = item.get("content");
                list.add(new OptionDTO(key == null ? null : String.valueOf(key), content == null ? null : String.valueOf(content)));
            }
            return list;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}

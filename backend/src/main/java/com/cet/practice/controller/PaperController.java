package com.cet.practice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cet.practice.common.BusinessException;
import com.cet.practice.common.Result;
import com.cet.practice.converter.PaperConverter;
import com.cet.practice.dto.*;
import com.cet.practice.entity.CetPaper;
import com.cet.practice.entity.CetPaperQuestion;
import com.cet.practice.service.CetPaperQuestionService;
import com.cet.practice.service.CetPaperService;
import com.cet.practice.service.CetPracticeRecordService;
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
            list.add(PaperConverter.toPaperDTO(p));
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
        return Result.success(PaperConverter.toPaperDTO(p));
    }

    @GetMapping("/{id}/questions")
    public Result<List<QuestionDTO>> getQuestions(@PathVariable Long id, @RequestParam(defaultValue = "practice") String mode) {
        CetPaper p = paperService.getById(id);
        if (p == null || (p.getDeleted() != null && p.getDeleted() == 1) || !"PUBLISHED".equalsIgnoreCase(p.getStatus())) {
            return Result.notFound("试卷不存在");
        }

        List<CetPaperQuestion> questions = paperService.getQuestionsByPaperId(id);

        boolean hideAnswer = "exam".equalsIgnoreCase(mode);
        List<QuestionDTO> dtos = new ArrayList<QuestionDTO>();
        for (CetPaperQuestion q : questions) {
            QuestionDTO dto = PaperConverter.toQuestionDTO(q);
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
}

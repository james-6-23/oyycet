package com.cet.practice.service;

import com.cet.practice.common.BusinessException;
import com.cet.practice.dto.*;
import com.cet.practice.entity.CetPaper;
import com.cet.practice.entity.CetPaperQuestion;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPaperService {

    private final CetPaperService paperService;
    private final CetPaperQuestionService questionService;
    private final ObjectMapper objectMapper;

    @Transactional
    public PaperDTO importFromJsonPayload(AdminImportPayload payload) {
        PaperImportDTO p = payload.getPaper();

        CetPaper paper = new CetPaper();
        paper.setTitle(p.getTitle());
        paper.setYear(p.getYear());
        paper.setMonth(p.getMonth());
        paper.setPaperNo(p.getPaperNo());
        paper.setDifficulty(p.getDifficulty() == null ? "MEDIUM" : p.getDifficulty());
        paper.setType(p.getType() == null ? "FULL" : p.getType());
        paper.setDurationMin(p.getDurationMin() == null ? 130 : p.getDurationMin());
        paper.setStatus("DRAFT");
        paper.setAttempts(0);
        paper.setListeningRefPaperNo(p.getListeningRefPaperNo());
        paperService.save(paper);

        List<CetPaperQuestion> toSave = new ArrayList<CetPaperQuestion>();
        for (QuestionImportDTO q : payload.getQuestions()) {
            CetPaperQuestion e = new CetPaperQuestion();
            e.setPaperId(paper.getId());
            e.setType(q.getType());
            e.setSubType(q.getSubType());
            e.setQuestionNo(q.getQuestionNo());
            e.setScore(q.getScore() == null ? new BigDecimal("1.00") : q.getScore());
            e.setContent(q.getContent());
            e.setCorrectAnswer(q.getCorrectAnswer());
            e.setExplanation(q.getExplanation());
            e.setPassage(q.getPassage());
            e.setAudioUrl(q.getAudioUrl());
            e.setAudioStartTime(q.getAudioStartTime());
            e.setAudioEndTime(q.getAudioEndTime());
            e.setPassageGroup(q.getPassageGroup());
            e.setDirections(q.getDirections());
            e.setSortOrder(q.getSortOrder() == null ? (q.getQuestionNo() == null ? 0 : q.getQuestionNo()) : q.getSortOrder());
            try {
                if (q.getOptions() != null) {
                    e.setOptionsJson(objectMapper.writeValueAsString(q.getOptions()));
                }
                if (q.getWordBank() != null) {
                    e.setWordBankJson(objectMapper.writeValueAsString(q.getWordBank()));
                }
            } catch (Exception ex) {
                throw new BusinessException(400, "题目JSON字段序列化失败");
            }
            toSave.add(e);
        }

        if (!toSave.isEmpty()) {
            questionService.saveBatch(toSave);
        }

        return toPaperDTO(paper);
    }

    public void publish(Long paperId) {
        CetPaper paper = paperService.getById(paperId);
        if (paper == null || (paper.getDeleted() != null && paper.getDeleted() == 1)) {
            throw new BusinessException(404, "试卷不存在");
        }
        paper.setStatus("PUBLISHED");
        paperService.updateById(paper);
    }

    public void unpublish(Long paperId) {
        CetPaper paper = paperService.getById(paperId);
        if (paper == null || (paper.getDeleted() != null && paper.getDeleted() == 1)) {
            throw new BusinessException(404, "试卷不存在");
        }
        paper.setStatus("DRAFT");
        paperService.updateById(paper);
    }

    private static PaperDTO toPaperDTO(CetPaper paper) {
        PaperDTO dto = new PaperDTO();
        dto.setId(paper.getId());
        dto.setTitle(paper.getTitle());
        dto.setYear(paper.getYear());
        dto.setMonth(paper.getMonth());
        dto.setPaperNo(paper.getPaperNo());
        dto.setDifficulty(paper.getDifficulty());
        dto.setType(paper.getType());
        dto.setDurationMin(paper.getDurationMin());
        dto.setStatus(paper.getStatus());
        dto.setAttempts(paper.getAttempts());
        dto.setListeningRefPaperId(paper.getListeningRefPaperId());
        dto.setListeningRefPaperNo(paper.getListeningRefPaperNo());
        return dto;
    }
}


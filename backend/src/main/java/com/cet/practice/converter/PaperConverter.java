package com.cet.practice.converter;

import com.cet.practice.dto.OptionDTO;
import com.cet.practice.dto.PaperDTO;
import com.cet.practice.dto.QuestionDTO;
import com.cet.practice.entity.CetPaper;
import com.cet.practice.entity.CetPaperQuestion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class PaperConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private PaperConverter() {
    }

    public static PaperDTO toPaperDTO(CetPaper paper) {
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

    public static QuestionDTO toQuestionDTO(CetPaperQuestion q) {
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

    static List<OptionDTO> parseOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            List<Map<String, Object>> arr = OBJECT_MAPPER.readValue(
                    optionsJson, new TypeReference<List<Map<String, Object>>>() {});
            List<OptionDTO> list = new ArrayList<>();
            for (Map<String, Object> item : arr) {
                Object key = item.get("key");
                Object content = item.get("content");
                list.add(new OptionDTO(
                        key == null ? null : String.valueOf(key),
                        content == null ? null : String.valueOf(content)));
            }
            return list;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}

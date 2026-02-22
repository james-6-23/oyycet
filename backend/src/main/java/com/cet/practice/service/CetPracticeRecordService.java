package com.cet.practice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cet.practice.common.BusinessException;
import com.cet.practice.dto.*;
import com.cet.practice.entity.CetPaper;
import com.cet.practice.entity.CetPaperQuestion;
import com.cet.practice.entity.CetPracticeRecord;
import com.cet.practice.mapper.CetPaperMapper;
import com.cet.practice.mapper.CetPaperQuestionMapper;
import com.cet.practice.mapper.CetPracticeRecordMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CetPracticeRecordService extends ServiceImpl<CetPracticeRecordMapper, CetPracticeRecord> {

    private final CetPaperMapper paperMapper;
    private final CetPaperQuestionMapper questionMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public SubmitPaperResponse submit(Long userId, Long paperId, Map<Long, String> answers, Integer durationSeconds) {
        List<CetPaperQuestion> questions = questionMapper.selectList(
                new LambdaQueryWrapper<CetPaperQuestion>()
                        .eq(CetPaperQuestion::getPaperId, paperId)
                        .eq(CetPaperQuestion::getDeleted, 0)
                        .orderByAsc(CetPaperQuestion::getSortOrder)
                        .orderByAsc(CetPaperQuestion::getId));

        if (questions == null || questions.isEmpty()) {
            throw new BusinessException(404, "试卷不存在或无题目");
        }

        int correctCount = 0;
        int totalCount = 0;
        BigDecimal totalScore = BigDecimal.ZERO;
        BigDecimal gotScore = BigDecimal.ZERO;
        List<Map<String, Object>> answerDetails = new ArrayList<Map<String, Object>>();

        for (CetPaperQuestion q : questions) {
            String userAnswer = answers.get(q.getId());
            String correctAnswer = q.getCorrectAnswer();
            boolean scorable = correctAnswer != null && !correctAnswer.trim().isEmpty();
            Boolean isCorrect = null;
            if (scorable) {
                totalCount++;
                BigDecimal qScore = q.getScore() == null ? BigDecimal.ONE : q.getScore();
                totalScore = totalScore.add(qScore);
                isCorrect = isAnswerCorrect(correctAnswer, userAnswer);
                if (Boolean.TRUE.equals(isCorrect)) {
                    correctCount++;
                    gotScore = gotScore.add(qScore);
                }
            }

            Map<String, Object> detail = new HashMap<String, Object>();
            detail.put("questionId", q.getId());
            detail.put("questionNo", q.getQuestionNo());
            detail.put("type", q.getType());
            detail.put("userAnswer", userAnswer);
            detail.put("correctAnswer", correctAnswer);
            detail.put("isCorrect", isCorrect);
            answerDetails.add(detail);
        }

        int score = 0;
        if (totalCount > 0 && totalScore.compareTo(BigDecimal.ZERO) > 0) {
            score = gotScore
                    .multiply(new BigDecimal("100"))
                    .divide(totalScore, 0, RoundingMode.HALF_UP)
                    .intValue();
        }

        CetPracticeRecord record = new CetPracticeRecord();
        record.setUserId(userId);
        record.setPaperId(paperId);
        record.setScore(score);
        record.setDurationSeconds(durationSeconds == null ? 0 : durationSeconds);
        record.setCorrectCount(correctCount);
        record.setTotalCount(totalCount);
        record.setFinishTime(LocalDateTime.now());
        try {
            record.setAnswerDetailJson(objectMapper.writeValueAsString(answerDetails));
        } catch (Exception e) {
            throw new BusinessException(500, "序列化答题详情失败");
        }
        save(record);

        paperMapper.incrementAttempts(paperId);

        SubmitPaperResponse resp = new SubmitPaperResponse();
        resp.setRecordId(record.getId());
        resp.setScore(score);
        resp.setCorrectCount(correctCount);
        resp.setTotalCount(totalCount);
        resp.setDurationSeconds(record.getDurationSeconds());
        return resp;
    }

    public Page<PracticeRecordDTO> pageMyRecords(Long userId, int current, int size) {
        Page<CetPracticeRecord> page = new Page<CetPracticeRecord>(current, size);
        Page<CetPracticeRecord> recordPage = page(page, new LambdaQueryWrapper<CetPracticeRecord>()
                .eq(CetPracticeRecord::getUserId, userId)
                .eq(CetPracticeRecord::getDeleted, 0)
                .orderByDesc(CetPracticeRecord::getFinishTime));

        List<CetPracticeRecord> records = recordPage.getRecords();
        List<Long> paperIds = new ArrayList<Long>();
        for (CetPracticeRecord r : records) {
            if (r.getPaperId() != null && !paperIds.contains(r.getPaperId())) {
                paperIds.add(r.getPaperId());
            }
        }
        Map<Long, CetPaper> paperMap = paperIds.isEmpty()
                ? Collections.<Long, CetPaper>emptyMap()
                : toPaperMap(paperMapper.selectBatchIds(paperIds));

        Page<PracticeRecordDTO> result = new Page<PracticeRecordDTO>(current, size, recordPage.getTotal());
        List<PracticeRecordDTO> dtoList = new ArrayList<PracticeRecordDTO>();
        for (CetPracticeRecord r : records) {
            PracticeRecordDTO dto = new PracticeRecordDTO();
            dto.setId(r.getId());
            dto.setPaperId(r.getPaperId());
            dto.setScore(r.getScore());
            dto.setDurationSeconds(r.getDurationSeconds());
            dto.setCorrectCount(r.getCorrectCount());
            dto.setTotalCount(r.getTotalCount());
            dto.setFinishTime(r.getFinishTime());

            CetPaper paper = paperMap.get(r.getPaperId());
            if (paper != null) {
                dto.setPaperTitle(paper.getTitle());
                dto.setPaperYear(paper.getYear());
                dto.setPaperType(paper.getType());
            }
            dtoList.add(dto);
        }
        result.setRecords(dtoList);
        return result;
    }

    public PracticeRecordDetailDTO getMyRecordDetail(Long userId, Long recordId) {
        CetPracticeRecord record = getOne(new LambdaQueryWrapper<CetPracticeRecord>()
                .eq(CetPracticeRecord::getId, recordId)
                .eq(CetPracticeRecord::getUserId, userId)
                .eq(CetPracticeRecord::getDeleted, 0)
                .last("LIMIT 1"));
        if (record == null) {
            return null;
        }

        PracticeRecordDetailDTO dto = new PracticeRecordDetailDTO();
        dto.setId(record.getId());
        dto.setPaperId(record.getPaperId());
        dto.setScore(record.getScore());
        dto.setDurationSeconds(record.getDurationSeconds());
        dto.setCorrectCount(record.getCorrectCount());
        dto.setTotalCount(record.getTotalCount());
        dto.setFinishTime(record.getFinishTime());

        CetPaper paper = paperMapper.selectById(record.getPaperId());
        if (paper != null) {
            dto.setPaperTitle(paper.getTitle());
            dto.setPaperYear(paper.getYear());
            dto.setPaperType(paper.getType());
        }

        List<Map<String, Object>> answerDetails = parseAnswerDetail(record.getAnswerDetailJson());
        List<Long> qIds = new ArrayList<Long>();
        for (Map<String, Object> a : answerDetails) {
            Object qId = a.get("questionId");
            if (qId != null) {
                Long id = Long.valueOf(String.valueOf(qId));
                if (!qIds.contains(id)) {
                    qIds.add(id);
                }
            }
        }
        Map<Long, CetPaperQuestion> qMap = qIds.isEmpty()
                ? Collections.<Long, CetPaperQuestion>emptyMap()
                : toQuestionMap(questionMapper.selectBatchIds(qIds));

        List<PracticeRecordQuestionDTO> questionDTOs = new ArrayList<PracticeRecordQuestionDTO>();
        long wrongCount = 0L;
        for (Map<String, Object> a : answerDetails) {
            Object qId = a.get("questionId");
            if (qId == null) {
                continue;
            }
            Long id = Long.valueOf(String.valueOf(qId));
            CetPaperQuestion q = qMap.get(id);

            PracticeRecordQuestionDTO qdto = new PracticeRecordQuestionDTO();
            qdto.setQuestionId(id);
            qdto.setQuestionNo(asInteger(a.get("questionNo")));
            qdto.setType(asString(a.get("type")));
            qdto.setUserAnswer(asString(a.get("userAnswer")));
            qdto.setCorrectAnswer(asString(a.get("correctAnswer")));
            Boolean isCorrect = asBoolean(a.get("isCorrect"));
            qdto.setIsCorrect(isCorrect);
            if (Boolean.FALSE.equals(isCorrect)) {
                wrongCount++;
            }
            if (q != null) {
                qdto.setContent(q.getContent());
                qdto.setOptionsJson(q.getOptionsJson());
                qdto.setExplanation(q.getExplanation());
                qdto.setPassage(q.getPassage());
            }
            questionDTOs.add(qdto);
        }

        dto.setWrongCount(wrongCount);
        dto.setQuestions(questionDTOs);
        return dto;
    }

    public PracticeStatsDTO getMyStats(Long userId) {
        Map<String, Object> row = baseMapper.getUserStats(userId);
        if (row == null) {
            row = Collections.emptyMap();
        }

        long totalPractices = asLong(row.get("total_records"));
        PracticeStatsDTO dto = new PracticeStatsDTO();
        dto.setTotalPractices(totalPractices);

        if (totalPractices > 0) {
            dto.setAverageScore(Math.round(asDouble(row.get("avg_score"))));
            dto.setTotalCorrect(asLong(row.get("total_correct")));
            dto.setTotalQuestions(asLong(row.get("total_questions")));
            long totalQuestions = dto.getTotalQuestions() == null ? 0L : dto.getTotalQuestions();
            long totalCorrect = dto.getTotalCorrect() == null ? 0L : dto.getTotalCorrect();
            dto.setAccuracy(totalQuestions > 0 ? Math.round(totalCorrect * 100.0 / totalQuestions) : 0L);
            dto.setTotalDuration(asLong(row.get("total_duration")));
        } else {
            dto.setAverageScore(0L);
            dto.setTotalCorrect(0L);
            dto.setTotalQuestions(0L);
            dto.setAccuracy(0L);
            dto.setTotalDuration(0L);
        }
        return dto;
    }

    private static boolean isAnswerCorrect(String correctAnswer, String userAnswer) {
        String ca = normalize(correctAnswer);
        String ua = normalize(userAnswer);
        if (ca == null || ua == null) {
            return false;
        }
        if (ca.contains("|")) {
            String[] parts = ca.split("\\|");
            for (String p : parts) {
                if (ua.equals(p.trim())) {
                    return true;
                }
            }
            return false;
        }
        return ca.equals(ua);
    }

    private static String normalize(String s) {
        if (s == null) {
            return null;
        }
        String v = s.trim();
        if (v.isEmpty()) {
            return null;
        }
        return v.toUpperCase();
    }

    private List<Map<String, Object>> parseAnswerDetail(String json) {
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static Map<Long, CetPaper> toPaperMap(List<CetPaper> papers) {
        Map<Long, CetPaper> map = new HashMap<Long, CetPaper>();
        for (CetPaper p : papers) {
            map.put(p.getId(), p);
        }
        return map;
    }

    private static Map<Long, CetPaperQuestion> toQuestionMap(List<CetPaperQuestion> questions) {
        Map<Long, CetPaperQuestion> map = new HashMap<Long, CetPaperQuestion>();
        for (CetPaperQuestion q : questions) {
            map.put(q.getId(), q);
        }
        return map;
    }

    private static String asString(Object v) {
        return v == null ? null : String.valueOf(v);
    }

    private static Integer asInteger(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof Number) {
            return ((Number) v).intValue();
        }
        try {
            return Integer.valueOf(String.valueOf(v));
        } catch (Exception e) {
            return null;
        }
    }

    private static Boolean asBoolean(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        return Boolean.valueOf(String.valueOf(v));
    }

    private static long asLong(Object v) {
        if (v == null) {
            return 0L;
        }
        if (v instanceof Number) {
            return ((Number) v).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(v));
        } catch (Exception e) {
            return 0L;
        }
    }

    private static double asDouble(Object v) {
        if (v == null) {
            return 0.0;
        }
        if (v instanceof Number) {
            return ((Number) v).doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(v));
        } catch (Exception e) {
            return 0.0;
        }
    }
}


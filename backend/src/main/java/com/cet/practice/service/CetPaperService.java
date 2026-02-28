package com.cet.practice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cet.practice.common.enums.PaperStatus;
import com.cet.practice.entity.CetPaper;
import com.cet.practice.entity.CetPaperQuestion;
import com.cet.practice.mapper.CetPaperMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CetPaperService extends ServiceImpl<CetPaperMapper, CetPaper> {

    private final CacheService cacheService;
    private final CetPaperQuestionService questionService;

    private static final String PAPERS_LIST_PREFIX = "papers:list:";
    private static final String PAPERS_QUESTIONS_PREFIX = "papers:questions:";
    private static final Duration LIST_TTL = Duration.ofMinutes(5);
    private static final Duration QUESTIONS_TTL = Duration.ofMinutes(30);

    @SuppressWarnings("unchecked")
    public Page<CetPaper> pagePublished(int current, int size,
                                        Integer year, Integer month, Integer paperNo,
                                        String type, String difficulty) {
        String cacheKey = PAPERS_LIST_PREFIX + buildParamsHash(current, size, year, month, paperNo, type, difficulty);
        Page<CetPaper> cached = cacheService.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        LambdaQueryWrapper<CetPaper> wrapper = new LambdaQueryWrapper<CetPaper>()
                .eq(CetPaper::getDeleted, 0)
                .eq(CetPaper::getStatus, PaperStatus.PUBLISHED.getValue());

        if (year != null) {
            wrapper.eq(CetPaper::getYear, year);
        }
        if (month != null) {
            wrapper.eq(CetPaper::getMonth, month);
        }
        if (paperNo != null) {
            wrapper.eq(CetPaper::getPaperNo, paperNo);
        }
        if (type != null && !type.trim().isEmpty() && !"ALL".equalsIgnoreCase(type.trim())) {
            wrapper.eq(CetPaper::getType, type.trim());
        }
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            wrapper.eq(CetPaper::getDifficulty, difficulty.trim());
        }

        wrapper.orderByDesc(CetPaper::getYear)
                .orderByDesc(CetPaper::getMonth)
                .orderByDesc(CetPaper::getPaperNo)
                .orderByDesc(CetPaper::getId);

        Page<CetPaper> result = page(new Page<CetPaper>(current, size), wrapper);
        cacheService.set(cacheKey, result, LIST_TTL);
        return result;
    }

    public List<CetPaperQuestion> getQuestionsByPaperId(Long paperId) {
        String cacheKey = PAPERS_QUESTIONS_PREFIX + paperId;
        List<CetPaperQuestion> cached = cacheService.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<CetPaperQuestion> questions = questionService.list(
                new LambdaQueryWrapper<CetPaperQuestion>()
                        .eq(CetPaperQuestion::getPaperId, paperId)
                        .eq(CetPaperQuestion::getDeleted, 0)
                        .orderByAsc(CetPaperQuestion::getSortOrder)
                        .orderByAsc(CetPaperQuestion::getId)
        );
        cacheService.set(cacheKey, questions, QUESTIONS_TTL);
        return questions;
    }

    public void evictPaperListCache() {
        cacheService.deleteByPrefix(PAPERS_LIST_PREFIX);
    }

    public void evictQuestionCache(Long paperId) {
        cacheService.delete(PAPERS_QUESTIONS_PREFIX + paperId);
    }

    private String buildParamsHash(int current, int size, Integer year, Integer month,
                                   Integer paperNo, String type, String difficulty) {
        return Objects.hash(current, size, year, month, paperNo,
                type == null ? null : type.trim().toLowerCase(),
                difficulty == null ? null : difficulty.trim().toLowerCase()) + "";
    }
}

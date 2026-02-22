package com.cet.practice.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cet.practice.entity.CetPaper;
import com.cet.practice.mapper.CetPaperMapper;
import org.springframework.stereotype.Service;

@Service
public class CetPaperService extends ServiceImpl<CetPaperMapper, CetPaper> {

    public Page<CetPaper> pagePublished(int current, int size,
                                        Integer year, Integer month, Integer paperNo,
                                        String type, String difficulty) {
        LambdaQueryWrapper<CetPaper> wrapper = new LambdaQueryWrapper<CetPaper>()
                .eq(CetPaper::getDeleted, 0)
                .eq(CetPaper::getStatus, "PUBLISHED");

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

        return page(new Page<CetPaper>(current, size), wrapper);
    }
}


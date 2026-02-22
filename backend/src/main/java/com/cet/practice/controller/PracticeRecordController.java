package com.cet.practice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cet.practice.common.Result;
import com.cet.practice.dto.PracticeRecordDetailDTO;
import com.cet.practice.dto.PracticeRecordDTO;
import com.cet.practice.dto.PracticeStatsDTO;
import com.cet.practice.service.CetPracticeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me/practice-records")
@RequiredArgsConstructor
public class PracticeRecordController {

    private final CetPracticeRecordService recordService;

    @GetMapping
    public Result<Page<PracticeRecordDTO>> pageMyRecords(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestAttribute("userId") Long userId) {
        return Result.success(recordService.pageMyRecords(userId, current, size));
    }

    @GetMapping("/{id}")
    public Result<PracticeRecordDetailDTO> getMyRecordDetail(
            @PathVariable Long id,
            @RequestAttribute("userId") Long userId) {
        PracticeRecordDetailDTO detail = recordService.getMyRecordDetail(userId, id);
        if (detail == null) {
            return Result.notFound("记录不存在");
        }
        return Result.success(detail);
    }

    @GetMapping("/stats")
    public Result<PracticeStatsDTO> getMyStats(@RequestAttribute("userId") Long userId) {
        return Result.success(recordService.getMyStats(userId));
    }
}


package com.cet.practice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cet.practice.common.Result;
import com.cet.practice.common.enums.PaperStatus;
import com.cet.practice.dto.AdminDashboardDTO;
import com.cet.practice.entity.CetPaper;
import com.cet.practice.entity.CetPracticeRecord;
import com.cet.practice.entity.CetUser;
import com.cet.practice.service.CetPaperService;
import com.cet.practice.service.CetPracticeRecordService;
import com.cet.practice.service.CetUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final CetUserService userService;
    private final CetPaperService paperService;
    private final CetPracticeRecordService recordService;

    @GetMapping("/stats")
    public Result<AdminDashboardDTO> getStats() {
        AdminDashboardDTO dto = new AdminDashboardDTO();

        dto.setTotalUsers(userService.count(new LambdaQueryWrapper<CetUser>()
                .eq(CetUser::getDeleted, 0)));

        dto.setTotalPapers(paperService.count(new LambdaQueryWrapper<CetPaper>()
                .eq(CetPaper::getDeleted, 0)));

        dto.setTotalPublished(paperService.count(new LambdaQueryWrapper<CetPaper>()
                .eq(CetPaper::getDeleted, 0)
                .eq(CetPaper::getStatus, PaperStatus.PUBLISHED.getValue())));

        dto.setTotalRecords(recordService.count(new LambdaQueryWrapper<CetPracticeRecord>()
                .eq(CetPracticeRecord::getDeleted, 0)));

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        dto.setTodayRecords(recordService.count(new LambdaQueryWrapper<CetPracticeRecord>()
                .eq(CetPracticeRecord::getDeleted, 0)
                .ge(CetPracticeRecord::getCreateTime, todayStart)));

        return Result.success(dto);
    }
}

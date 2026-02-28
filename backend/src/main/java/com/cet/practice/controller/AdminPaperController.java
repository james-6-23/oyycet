package com.cet.practice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cet.practice.common.BusinessException;
import com.cet.practice.common.Result;
import com.cet.practice.dto.AdminImportPayload;
import com.cet.practice.dto.PaperDTO;
import com.cet.practice.service.AdminPaperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/admin/papers")
@RequiredArgsConstructor
public class AdminPaperController {

    private final AdminPaperService adminPaperService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public Result<Page<PaperDTO>> list(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String title) {
        return Result.success(adminPaperService.pagePapers(current, size, status, title));
    }

    @PostMapping("/import-json")
    public Result<PaperDTO> importJson(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.badRequest("请选择要上传的JSON文件");
        }
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".json")) {
            return Result.badRequest("只支持JSON格式文件");
        }

        try {
            String json = new String(file.getBytes(), StandardCharsets.UTF_8);
            AdminImportPayload payload = objectMapper.readValue(json, AdminImportPayload.class);
            if (payload.getPaper() == null || payload.getPaper().getTitle() == null || payload.getPaper().getTitle().trim().isEmpty()) {
                throw new BusinessException(400, "paper.title 不能为空");
            }
            if (payload.getQuestions() == null || payload.getQuestions().isEmpty()) {
                throw new BusinessException(400, "questions 不能为空");
            }
            return Result.success(adminPaperService.importFromJsonPayload(payload));
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(400, "解析JSON失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/publish")
    public Result<Void> publish(@PathVariable Long id) {
        adminPaperService.publish(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/unpublish")
    public Result<Void> unpublish(@PathVariable Long id) {
        adminPaperService.unpublish(id);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminPaperService.deletePaper(id);
        return Result.success(null);
    }
}

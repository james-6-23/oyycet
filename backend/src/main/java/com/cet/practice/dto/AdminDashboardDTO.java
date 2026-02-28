package com.cet.practice.dto;

import lombok.Data;

@Data
public class AdminDashboardDTO {
    private long totalUsers;
    private long totalPapers;
    private long totalPublished;
    private long totalRecords;
    private long todayRecords;
}

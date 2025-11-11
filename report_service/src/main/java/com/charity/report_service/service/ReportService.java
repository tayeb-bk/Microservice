package com.charity.report_service.service;

import com.charity.report_service.entity.Report;
import com.charity.report_service.enums.ReportStatus;
import com.charity.report_service.enums.ReportType;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    List<Report> getAllReports();

    Report getReportById(Long id);

    Report createReport(Report report);

    Report updateReport(Long id, Report reportDetails);

    Report updateReportStatus(Long id, ReportStatus status);

    void deleteReport(Long id);

    List<Report> getReportsByType(ReportType type);

    List<Report> getReportsByStatus(ReportStatus status);

    List<Report> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Report generateReport(ReportType type, LocalDateTime startDate, LocalDateTime endDate);

    List<Report> getReportsByUser(String user);

    List<Report> getRecentReports();

    Report generateDonationReport(LocalDateTime start, LocalDateTime end, String generatedBy);

    Report generateEventReport(LocalDateTime start, LocalDateTime end, String generatedBy);

    Report generateCampaignReport(LocalDateTime start, LocalDateTime end, String generatedBy);

    Report generateFormationReport(LocalDateTime start, LocalDateTime end, String generatedBy);

    Report generateBlogReport(LocalDateTime start, LocalDateTime end, String generatedBy);

    Report generateNotificationReport(LocalDateTime start, LocalDateTime end, String generatedBy);

    Report generateGeneralReport(LocalDateTime start, LocalDateTime end, String generatedBy);

    Long getReportCountByStatus(ReportStatus status);
}
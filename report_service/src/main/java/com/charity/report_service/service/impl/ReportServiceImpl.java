package com.charity.report_service.service.impl;

import com.charity.report_service.entity.Report;
import com.charity.report_service.enums.ReportStatus;
import com.charity.report_service.enums.ReportType;
import com.charity.report_service.exception.ReportNotFoundException;
import com.charity.report_service.repository.ReportRepository;
import com.charity.report_service.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + id));
    }

    @Override
    public Report createReport(Report report) {
        // Set default values if not provided
        if (report.getStatus() == null) {
            report.setStatus(ReportStatus.PENDING);
        }

        return reportRepository.save(report);
    }

    @Override
    public Report updateReport(Long id, Report reportDetails) {
        Report existingReport = getReportById(id);

        // Update fields
        existingReport.setTitle(reportDetails.getTitle());
        existingReport.setDescription(reportDetails.getDescription());
        existingReport.setType(reportDetails.getType());
        existingReport.setPeriodStart(reportDetails.getPeriodStart());
        existingReport.setPeriodEnd(reportDetails.getPeriodEnd());
        existingReport.setGeneratedBy(reportDetails.getGeneratedBy());

        return reportRepository.save(existingReport);
    }

    @Override
    public Report updateReportStatus(Long id, ReportStatus status) {
        Report report = getReportById(id);
        report.setStatus(status);
        return reportRepository.save(report);
    }

    @Override
    public void deleteReport(Long id) {
        Report report = getReportById(id);
        reportRepository.delete(report);
    }

    @Override
    public List<Report> getReportsByType(ReportType type) {
        return reportRepository.findByType(type);
    }

    @Override
    public List<Report> getReportsByStatus(ReportStatus status) {
        return reportRepository.findByStatus(status);
    }

    @Override
    public List<Report> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return reportRepository.findByCreatedDateBetween(startDate, endDate);
    }

    @Override
    public Report generateReport(ReportType type, LocalDateTime startDate, LocalDateTime endDate) {
        // Create a new report for generation
        Report report = new Report();
        report.setTitle("Generated " + type.getDisplayName());
        report.setType(type);
        report.setPeriodStart(startDate);
        report.setPeriodEnd(endDate);
        report.setGeneratedBy("system");
        report.setStatus(ReportStatus.IN_PROGRESS);
        report.setDescription("Auto-generated report for period: " + startDate + " to " + endDate);

        // Save and return the report
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getReportsByUser(String user) {
        return reportRepository.findByGeneratedBy(user);
    }

    @Override
    public List<Report> getRecentReports() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return reportRepository.findByCreatedDateAfterOrderByCreatedDateDesc(sevenDaysAgo);
    }

    @Override
    public Report generateDonationReport(LocalDateTime start, LocalDateTime end, String generatedBy) {
        Report report = new Report();
        report.setTitle("Donation Report - " + start.toLocalDate() + " to " + end.toLocalDate());
        report.setType(ReportType.DONATION);
        report.setPeriodStart(start);
        report.setPeriodEnd(end);
        report.setGeneratedBy(generatedBy);
        report.setStatus(ReportStatus.IN_PROGRESS);
        report.setDescription("Generated donation report for specified period");

        return reportRepository.save(report);
    }

    @Override
    public Report generateEventReport(LocalDateTime start, LocalDateTime end, String generatedBy) {
        Report report = new Report();
        report.setTitle("Event Report - " + start.toLocalDate() + " to " + end.toLocalDate());
        report.setType(ReportType.EVENT);
        report.setPeriodStart(start);
        report.setPeriodEnd(end);
        report.setGeneratedBy(generatedBy);
        report.setStatus(ReportStatus.IN_PROGRESS);
        report.setDescription("Generated event report for specified period");

        return reportRepository.save(report);
    }

    @Override
    public Report generateCampaignReport(LocalDateTime start, LocalDateTime end, String generatedBy) {
        Report report = new Report();
        report.setTitle("Campaign Report - " + start.toLocalDate() + " to " + end.toLocalDate());
        report.setType(ReportType.CAMPAIGN);
        report.setPeriodStart(start);
        report.setPeriodEnd(end);
        report.setGeneratedBy(generatedBy);
        report.setStatus(ReportStatus.IN_PROGRESS);
        report.setDescription("Generated campaign report for specified period");

        return reportRepository.save(report);
    }

    @Override
    public Report generateFormationReport(LocalDateTime start, LocalDateTime end, String generatedBy) {
        Report report = new Report();
        report.setTitle("Formation Report - " + start.toLocalDate() + " to " + end.toLocalDate());
        report.setType(ReportType.FORMATION);
        report.setPeriodStart(start);
        report.setPeriodEnd(end);
        report.setGeneratedBy(generatedBy);
        report.setStatus(ReportStatus.IN_PROGRESS);
        report.setDescription("Generated formation/training report for specified period");

        return reportRepository.save(report);
    }

    @Override
    public Report generateBlogReport(LocalDateTime start, LocalDateTime end, String generatedBy) {
        Report report = new Report();
        report.setTitle("Blog Report - " + start.toLocalDate() + " to " + end.toLocalDate());
        report.setType(ReportType.BLOG);
        report.setPeriodStart(start);
        report.setPeriodEnd(end);
        report.setGeneratedBy(generatedBy);
        report.setStatus(ReportStatus.IN_PROGRESS);
        report.setDescription("Generated blog analytics report for specified period");

        return reportRepository.save(report);
    }

    @Override
    public Report generateNotificationReport(LocalDateTime start, LocalDateTime end, String generatedBy) {
        Report report = new Report();
        report.setTitle("Notification Report - " + start.toLocalDate() + " to " + end.toLocalDate());
        report.setType(ReportType.NOTIFICATION);
        report.setPeriodStart(start);
        report.setPeriodEnd(end);
        report.setGeneratedBy(generatedBy);
        report.setStatus(ReportStatus.IN_PROGRESS);
        report.setDescription("Generated notification system report for specified period");

        return reportRepository.save(report);
    }

    @Override
    public Report generateGeneralReport(LocalDateTime start, LocalDateTime end, String generatedBy) {
        Report report = new Report();
        report.setTitle("General Report - " + start.toLocalDate() + " to " + end.toLocalDate());
        report.setType(ReportType.GENERAL);
        report.setPeriodStart(start);
        report.setPeriodEnd(end);
        report.setGeneratedBy(generatedBy);
        report.setStatus(ReportStatus.IN_PROGRESS);
        report.setDescription("Generated general report for specified period");

        return reportRepository.save(report);
    }

    @Override
    public Long getReportCountByStatus(ReportStatus status) {
        return reportRepository.countByStatus(status);
    }
}
package com.charity.report_service.controller;

import com.charity.report_service.entity.Report;
import com.charity.report_service.enums.ReportType;
import com.charity.report_service.enums.ReportStatus;
import com.charity.report_service.dto.ReportRequest;
import com.charity.report_service.service.ReportService;
import com.charity.report_service.service.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Report report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Report>> getReportsByType(@PathVariable ReportType type) {
        List<Report> reports = reportService.getReportsByType(type);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/user/{user}")
    public ResponseEntity<List<Report>> getReportsByUser(@PathVariable String user) {
        List<Report> reports = reportService.getReportsByUser(user);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Report>> getRecentReports() {
        List<Report> reports = reportService.getRecentReports();
        return ResponseEntity.ok(reports);
    }

    @PostMapping
    public ResponseEntity<Report> createReport(@Valid @RequestBody ReportRequest request) {
        Report report = new Report();
        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setType(request.getType());
        report.setPeriodStart(request.getPeriodStart());
        report.setPeriodEnd(request.getPeriodEnd());
        report.setGeneratedBy(request.getGeneratedBy());

        Report savedReport = reportService.createReport(report);
        return ResponseEntity.ok(savedReport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(
            @PathVariable Long id,
            @Valid @RequestBody ReportRequest request) {

        Report report = new Report();
        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setType(request.getType());
        report.setPeriodStart(request.getPeriodStart());
        report.setPeriodEnd(request.getPeriodEnd());
        report.setGeneratedBy(request.getGeneratedBy());

        Report updatedReport = reportService.updateReport(id, report);
        return ResponseEntity.ok(updatedReport);
    }

    @PostMapping("/generate/donation")
    public ResponseEntity<Report> generateDonationReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String generatedBy) {

        Report report = reportService.generateDonationReport(start, end, generatedBy);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/generate/event")
    public ResponseEntity<Report> generateEventReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String generatedBy) {

        Report report = reportService.generateEventReport(start, end, generatedBy);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/generate/campaign")
    public ResponseEntity<Report> generateCampaignReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String generatedBy) {

        Report report = reportService.generateCampaignReport(start, end, generatedBy);
        return ResponseEntity.ok(report);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Report> updateReportStatus(
            @PathVariable Long id,
            @RequestParam ReportStatus status) {

        Report report = reportService.updateReportStatus(id, status);
        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/count/{status}")
    public ResponseEntity<Long> getReportCountByStatus(@PathVariable ReportStatus status) {
        Long count = reportService.getReportCountByStatus(status);
        return ResponseEntity.ok(count);
    }

    // PDF Generation Endpoint
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
    @GetMapping("/{id}/download-pdf")
    public ResponseEntity<byte[]> downloadReportPdf(@PathVariable Long id) {
        Report report = reportService.getReportById(id);

        byte[] pdfBytes = pdfGeneratorService.generateReportPdf(report);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("report-" + report.getId() + ".pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Report Service is running!");
    }
}
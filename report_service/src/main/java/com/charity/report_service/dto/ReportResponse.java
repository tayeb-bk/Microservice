package com.charity.report_service.dto;

import com.charity.report_service.enums.ReportType;
import com.charity.report_service.enums.ReportStatus;
import java.time.LocalDateTime;
import java.util.List;

public class ReportResponse {

    private Long id;
    private String title;
    private String description;
    private ReportType type;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String generatedBy;
    private ReportStatus status;
    private String filePath;
    private List<ReportDataDto> reportData;

    // Constructors
    public ReportResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ReportType getType() { return type; }
    public void setType(ReportType type) { this.type = type; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }

    public LocalDateTime getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDateTime periodStart) { this.periodStart = periodStart; }

    public LocalDateTime getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDateTime periodEnd) { this.periodEnd = periodEnd; }

    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }

    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public List<ReportDataDto> getReportData() { return reportData; }
    public void setReportData(List<ReportDataDto> reportData) { this.reportData = reportData; }

    // Inner class for report data
    public static class ReportDataDto {
        private String dataKey;
        private String dataValue;
        private String dataType;
        private Integer displayOrder;

        // Constructors, getters, setters
        public ReportDataDto() {}

        public ReportDataDto(String dataKey, String dataValue, String dataType) {
            this.dataKey = dataKey;
            this.dataValue = dataValue;
            this.dataType = dataType;
        }

        public String getDataKey() { return dataKey; }
        public void setDataKey(String dataKey) { this.dataKey = dataKey; }

        public String getDataValue() { return dataValue; }
        public void setDataValue(String dataValue) { this.dataValue = dataValue; }

        public String getDataType() { return dataType; }
        public void setDataType(String dataType) { this.dataType = dataType; }

        public Integer getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    }
}
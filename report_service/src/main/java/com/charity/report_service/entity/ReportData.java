package com.charity.report_service.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "report_data")
public class ReportData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    @JsonIgnore
    private Report report;

    @Column(name = "data_key", nullable = false)
    private String dataKey;

    @Column(name = "data_value", length = 2000)
    private String dataValue;

    @Column(name = "data_type")
    private String dataType; // NUMBER, TEXT, PERCENTAGE, DATE

    @Column(name = "display_order")
    private Integer displayOrder;

    // Constructors
    public ReportData() {}

    public ReportData(String dataKey, String dataValue, String dataType) {
        this.dataKey = dataKey;
        this.dataValue = dataValue;
        this.dataType = dataType;
    }

    public ReportData(String dataKey, String dataValue, String dataType, Integer displayOrder) {
        this.dataKey = dataKey;
        this.dataValue = dataValue;
        this.dataType = dataType;
        this.displayOrder = displayOrder;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Report getReport() { return report; }
    public void setReport(Report report) { this.report = report; }

    public String getDataKey() { return dataKey; }
    public void setDataKey(String dataKey) { this.dataKey = dataKey; }

    public String getDataValue() { return dataValue; }
    public void setDataValue(String dataValue) { this.dataValue = dataValue; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}
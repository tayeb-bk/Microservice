package com.charity.report_service.repository;

import com.charity.report_service.entity.ReportData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportDataRepository extends JpaRepository<ReportData, Long> {

    List<ReportData> findByReportId(Long reportId);

    List<ReportData> findByDataKey(String dataKey);

    void deleteByReportId(Long reportId);

    @Query("SELECT rd FROM ReportData rd WHERE rd.report.id = :reportId ORDER BY rd.displayOrder ASC")
    List<ReportData> findByReportIdOrderByDisplayOrder(@Param("reportId") Long reportId);

    @Query("SELECT rd FROM ReportData rd WHERE rd.dataType = :dataType")
    List<ReportData> findByDataType(@Param("dataType") String dataType);
}
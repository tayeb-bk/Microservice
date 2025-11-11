package com.charity.report_service.repository;

import com.charity.report_service.entity.Report;
import com.charity.report_service.enums.ReportType;
import com.charity.report_service.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByType(ReportType type);

    List<Report> findByStatus(ReportStatus status);

    List<Report> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

    List<Report> findByGeneratedBy(String user);

    // Added method for getRecentReports() in controller
    List<Report> findByCreatedDateAfterOrderByCreatedDateDesc(LocalDateTime date);

    @Query("SELECT r FROM Report r WHERE r.type = :type AND r.createdDate >= :startDate ORDER BY r.createdDate DESC")
    List<Report> findRecentReportsByType(@Param("type") ReportType type,
                                         @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = :status")
    Long countByStatus(@Param("status") ReportStatus status);

    @Query("SELECT r FROM Report r WHERE r.generatedBy = :user ORDER BY r.createdDate DESC")
    List<Report> findByGeneratedByOrderByCreatedDateDesc(@Param("user") String user);

    @Query("SELECT r FROM Report r WHERE r.periodStart >= :start AND r.periodEnd <= :end")
    List<Report> findByReportPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Report> findTop10ByOrderByCreatedDateDesc();
}
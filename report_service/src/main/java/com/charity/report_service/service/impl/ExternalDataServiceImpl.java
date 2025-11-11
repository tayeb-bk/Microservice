package com.charity.report_service.service.impl;

import com.charity.report_service.client.DonationServiceClient;
import com.charity.report_service.client.EventServiceClient;
import com.charity.report_service.service.ExternalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExternalDataServiceImpl implements ExternalDataService {

    @Autowired
    private DonationServiceClient donationServiceClient;

    @Autowired
    private EventServiceClient eventServiceClient;

    @Override
    public Map<String, Object> getDonationData(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> data = new HashMap<>();

        try {
            List<Object> donations = donationServiceClient.getDonationsByPeriod(startDate, endDate);
            Long totalCount = donationServiceClient.getTotalDonationCount();
            Double totalAmount = donationServiceClient.getTotalDonationAmount();

            data.put("donations", donations);
            data.put("totalCount", totalCount);
            data.put("totalAmount", totalAmount);
            data.put("periodStart", startDate);
            data.put("periodEnd", endDate);
            data.put("serviceStatus", "AVAILABLE");

        } catch (Exception e) {
            data.put("error", "Donation service unavailable: " + e.getMessage());
            data.put("totalCount", 0L);
            data.put("totalAmount", 0.0);
            data.put("serviceStatus", "UNAVAILABLE");
        }

        return data;
    }

    @Override
    public Map<String, Object> getEventData(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> data = new HashMap<>();

        try {
            List<Object> events = eventServiceClient.getEventsByPeriod(startDate, endDate);
            Long totalCount = eventServiceClient.getTotalEventCount();
            Long totalAttendees = eventServiceClient.getTotalAttendees();

            data.put("events", events);
            data.put("totalCount", totalCount);
            data.put("totalAttendees", totalAttendees);
            data.put("periodStart", startDate);
            data.put("periodEnd", endDate);
            data.put("serviceStatus", "AVAILABLE");

        } catch (Exception e) {
            data.put("error", "Event service unavailable: " + e.getMessage());
            data.put("totalCount", 0L);
            data.put("totalAttendees", 0L);
            data.put("serviceStatus", "UNAVAILABLE");
        }

        return data;
    }

    @Override
    public Map<String, Object> getVolunteerData(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> data = new HashMap<>();

        // Placeholder implementation - volunteer service not implemented yet
        data.put("totalVolunteers", 0L);
        data.put("totalHours", 0.0);
        data.put("activeVolunteers", 0L);
        data.put("periodStart", startDate);
        data.put("periodEnd", endDate);
        data.put("serviceStatus", "NOT_IMPLEMENTED");
        data.put("note", "Volunteer service not implemented yet");

        return data;
    }

    @Override
    public Map<String, Object> getFinancialData(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> financialData = new HashMap<>();

        // Get donation data as primary financial source
        Map<String, Object> donationData = getDonationData(startDate, endDate);

        // Calculate financial metrics
        Double totalRevenue = (Double) donationData.getOrDefault("totalAmount", 0.0);
        Long totalTransactions = (Long) donationData.getOrDefault("totalCount", 0L);

        financialData.put("donations", donationData);
        financialData.put("totalRevenue", totalRevenue);
        financialData.put("totalTransactions", totalTransactions);
        financialData.put("averageTransactionAmount",
                totalTransactions > 0 ? totalRevenue / totalTransactions : 0.0);
        financialData.put("periodStart", startDate);
        financialData.put("periodEnd", endDate);

        // Determine overall financial service status
        String donationStatus = (String) donationData.getOrDefault("serviceStatus", "UNAVAILABLE");
        financialData.put("serviceStatus", donationStatus);

        return financialData;
    }

    @Override
    public boolean isServiceAvailable(String serviceName) {
        try {
            switch (serviceName.toLowerCase()) {
                case "donation":
                case "gestion-don":
                    Long donationCount = donationServiceClient.getTotalDonationCount();
                    return donationCount != null;

                case "event":
                case "gestion-evenements":
                    Long eventCount = eventServiceClient.getTotalEventCount();
                    return eventCount != null;

                case "volunteer":
                    return false; // Not implemented yet

                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get comprehensive report data combining all external services
     */
    public Map<String, Object> getComprehensiveReportData(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> comprehensiveData = new HashMap<>();

        // Gather data from all services
        Map<String, Object> donationData = getDonationData(startDate, endDate);
        Map<String, Object> eventData = getEventData(startDate, endDate);
        Map<String, Object> volunteerData = getVolunteerData(startDate, endDate);
        Map<String, Object> financialData = getFinancialData(startDate, endDate);

        comprehensiveData.put("donations", donationData);
        comprehensiveData.put("events", eventData);
        comprehensiveData.put("volunteers", volunteerData);
        comprehensiveData.put("financial", financialData);
        comprehensiveData.put("reportGeneratedAt", LocalDateTime.now());
        comprehensiveData.put("periodStart", startDate);
        comprehensiveData.put("periodEnd", endDate);

        // Service availability summary
        Map<String, Boolean> serviceAvailability = new HashMap<>();
        serviceAvailability.put("donationService", isServiceAvailable("donation"));
        serviceAvailability.put("eventService", isServiceAvailable("event"));
        serviceAvailability.put("volunteerService", isServiceAvailable("volunteer"));

        comprehensiveData.put("serviceAvailability", serviceAvailability);

        return comprehensiveData;
    }

    /**
     * Get summary statistics across all services
     */
    public Map<String, Object> getSummaryStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> summary = new HashMap<>();

        Map<String, Object> donationData = getDonationData(startDate, endDate);
        Map<String, Object> eventData = getEventData(startDate, endDate);

        // Summary metrics
        summary.put("totalDonations", donationData.getOrDefault("totalCount", 0L));
        summary.put("totalDonationAmount", donationData.getOrDefault("totalAmount", 0.0));
        summary.put("totalEvents", eventData.getOrDefault("totalCount", 0L));
        summary.put("totalAttendees", eventData.getOrDefault("totalAttendees", 0L));

        // Calculate ratios and insights
        Long totalEvents = (Long) eventData.getOrDefault("totalCount", 0L);
        Long totalAttendees = (Long) eventData.getOrDefault("totalAttendees", 0L);

        summary.put("averageAttendeesPerEvent",
                totalEvents > 0 ? (double) totalAttendees / totalEvents : 0.0);

        summary.put("periodStart", startDate);
        summary.put("periodEnd", endDate);
        summary.put("generatedAt", LocalDateTime.now());

        return summary;
    }
}
package com.charity.report_service.service;

import java.time.LocalDateTime;
import java.util.Map;

public interface ExternalDataService {

    Map<String, Object> getDonationData(LocalDateTime startDate, LocalDateTime endDate);

    Map<String, Object> getEventData(LocalDateTime startDate, LocalDateTime endDate);

    Map<String, Object> getVolunteerData(LocalDateTime startDate, LocalDateTime endDate);

    Map<String, Object> getFinancialData(LocalDateTime startDate, LocalDateTime endDate);

    boolean isServiceAvailable(String serviceName);
}
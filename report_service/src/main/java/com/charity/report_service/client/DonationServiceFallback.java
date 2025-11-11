package com.charity.report_service.client;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DonationServiceFallback implements DonationServiceClient {

    @Override
    public List<Object> getDonationsByPeriod(LocalDateTime start, LocalDateTime end) {
        // Return empty list when service is unavailable
        return new ArrayList<>();
    }

    @Override
    public Long getTotalDonationCount() {
        // Return 0 when service is unavailable
        return 0L;
    }

    @Override
    public Double getTotalDonationAmount() {
        // Return 0.0 when service is unavailable
        return 0.0;
    }
}
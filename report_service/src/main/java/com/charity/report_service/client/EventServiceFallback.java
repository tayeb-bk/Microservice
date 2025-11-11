package com.charity.report_service.client;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventServiceFallback implements EventServiceClient {

    @Override
    public List<Object> getEventsByPeriod(LocalDateTime start, LocalDateTime end) {
        // Return empty list when service is unavailable
        return new ArrayList<>();
    }

    @Override
    public Long getTotalEventCount() {
        // Return 0 when service is unavailable
        return 0L;
    }

    @Override
    public Long getTotalAttendees() {
        // Return 0 when service is unavailable
        return 0L;
    }
}
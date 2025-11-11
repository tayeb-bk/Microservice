package com.charity.report_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "gestion-evenements", fallback = EventServiceFallback.class) // ✅ Keep as is (no underscores)
@Primary
public interface EventServiceClient {

    @GetMapping("/api/events/period")
    List<Object> getEventsByPeriod(
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end
    );

    @GetMapping("/api/events/count")
    Long getTotalEventCount();

    @GetMapping("/api/events/attendees/total")
    Long getTotalAttendees();
}
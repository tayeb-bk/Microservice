package com.charity.report_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "gestion-don", fallback = DonationServiceFallback.class) // ✅ Changed _ to -
@Primary
public interface DonationServiceClient {

    @GetMapping("/api/donations/period")
    List<Object> getDonationsByPeriod(
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end
    );

    @GetMapping("/api/donations/count")
    Long getTotalDonationCount();

    @GetMapping("/api/donations/total-amount")
    Double getTotalDonationAmount();
}
package com.smartsla.smart_sla_tracker.controller;



import com.smartsla.smart_sla_tracker.dto.SlaByPriorityDto;
import com.smartsla.smart_sla_tracker.dto.SlaSummaryDto;
import com.smartsla.smart_sla_tracker.service.SlaAnalyticsService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/analytics")
public class AnalyticsController {

    private final SlaAnalyticsService analyticsService;

    public AnalyticsController(SlaAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // ✅ OVERALL SLA SUMMARY
    @GetMapping("/summary")
    public SlaSummaryDto getSummary() {
        return analyticsService.getOverallSummary();
    }

    // ✅ SLA BY PRIORITY
    @GetMapping("/priority")
    public List<SlaByPriorityDto> getByPriority() {
        return analyticsService.getSlaByPriority();
    }
}


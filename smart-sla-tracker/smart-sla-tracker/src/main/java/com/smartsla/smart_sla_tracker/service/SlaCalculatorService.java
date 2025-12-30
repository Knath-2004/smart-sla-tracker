package com.smartsla.smart_sla_tracker.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import com.smartsla.smart_sla_tracker.entity.Priority;

@Service
public class SlaCalculatorService {

    public LocalDateTime calculateSlaDeadline(Priority priority) {
        LocalDateTime now = LocalDateTime.now();

        return switch (priority) {
            case HIGH -> now.plusMinutes(1);
            case MEDIUM -> now.plusMinutes(3);
            case LOW -> now.plusMinutes(5);
        };
    }
}

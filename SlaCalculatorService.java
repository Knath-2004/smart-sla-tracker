package com.smartsla.smart_sla_tracker.service;



import com.smartsla.smart_sla_tracker.entity.Priority;
import com.smartsla.smart_sla_tracker.entity.SlaPolicy;
import com.smartsla.smart_sla_tracker.repository.SlaPolicyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SlaCalculatorService {

    private final SlaPolicyRepository slaPolicyRepository;

    public SlaCalculatorService(SlaPolicyRepository slaPolicyRepository) {
        this.slaPolicyRepository = slaPolicyRepository;
    }

    public LocalDateTime calculateSlaDeadline(Priority priority) {

        SlaPolicy policy = slaPolicyRepository
                .findByPriority(priority)
                .orElseThrow(
                        () -> new RuntimeException(
                                "SLA Policy not configured for " + priority
                        )
                );

        return LocalDateTime.now()
                .plusHours(policy.getResolutionTimeHours());
    }
}

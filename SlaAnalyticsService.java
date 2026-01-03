package com.smartsla.smart_sla_tracker.service;



import com.smartsla.smart_sla_tracker.dto.SlaByPriorityDto;
import com.smartsla.smart_sla_tracker.dto.SlaSummaryDto;
import com.smartsla.smart_sla_tracker.entity.Priority;
import com.smartsla.smart_sla_tracker.repository.TicketRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SlaAnalyticsService {

    private final TicketRepository ticketRepository;

    public SlaAnalyticsService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // ✅ Overall SLA summary
    public SlaSummaryDto getOverallSummary() {

        long total = ticketRepository.count();
        long breached = ticketRepository.countByEscalatedTrue();

        return new SlaSummaryDto(total, breached);
    }

    // ✅ SLA by priority
    public List<SlaByPriorityDto> getSlaByPriority() {

        List<SlaByPriorityDto> result = new ArrayList<>();

        for (Priority priority : Priority.values()) {
            long total =
                    ticketRepository.countByPriority(priority);
            long breached =
                    ticketRepository.countByPriorityAndEscalatedTrue(priority);

            result.add(
                    new SlaByPriorityDto(priority, total, breached)
            );
        }

        return result;
    }
}


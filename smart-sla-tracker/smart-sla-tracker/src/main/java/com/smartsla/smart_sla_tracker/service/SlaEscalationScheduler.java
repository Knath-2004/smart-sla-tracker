package com.smartsla.smart_sla_tracker.service;

import com.smartsla.smart_sla_tracker.entity.Ticket;
import com.smartsla.smart_sla_tracker.entity.TicketStatus;
import com.smartsla.smart_sla_tracker.repository.TicketRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class SlaEscalationScheduler {

    private final TicketRepository ticketRepository;

    public SlaEscalationScheduler(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void escalateBreachedTickets() {

        List<Ticket> breachedTickets =
                ticketRepository.findBySlaDeadlineBeforeAndEscalatedFalse(LocalDateTime.now());

        for (Ticket ticket : breachedTickets) {
            ticket.setStatus(TicketStatus.ESCALATED);
            ticket.setEscalated(true);

            ticketRepository.save(ticket);

            // ✅ TEMP notification (console)
            System.out.println(
                    "🚨 SLA BREACHED | Ticket ID = " + ticket.getId()
            );
        }
    }
}

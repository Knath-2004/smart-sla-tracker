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
    private final EmailNotificationService emailNotificationService;
    private final TicketAuditService ticketAuditService;

    public SlaEscalationScheduler(
            TicketRepository ticketRepository,
            EmailNotificationService emailNotificationService,
            TicketAuditService ticketAuditService
    ) {
        this.ticketRepository = ticketRepository;
        this.emailNotificationService = emailNotificationService;
        this.ticketAuditService = ticketAuditService;
    }

    /**
     * ⏱ Runs every 1 minute
     * - Finds SLA-breached tickets
     * - Escalates them
     * - Sends email notification
     * - Logs audit
     */
    @Scheduled(fixedRate = 60000)
    public void escalateBreachedTickets() {

        List<Ticket> breachedTickets =
                ticketRepository.findBySlaDeadlineBeforeAndEscalatedFalse(
                        LocalDateTime.now()
                );

        for (Ticket ticket : breachedTickets) {

            TicketStatus oldStatus = ticket.getStatus();

            ticket.setStatus(TicketStatus.ESCALATED);
            ticket.setEscalated(true);
            ticketRepository.save(ticket);

            // 📧 SEND EMAIL
            emailNotificationService.sendSlaBreachEmail(ticket);

            // 📝 AUDIT LOG
            ticketAuditService.log(
                    ticket.getId(),
                    "AUTO_ESCALATION",
                    oldStatus.name(),
                    TicketStatus.ESCALATED.name(),
                    "SYSTEM"
            );

            // 🖥️ LOG (Optional)
            System.out.println(
                    "🚨 SLA BREACHED | Ticket ID = " + ticket.getId()
            );
        }
    }
}



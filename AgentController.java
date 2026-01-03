package com.smartsla.smart_sla_tracker.controller;

import com.smartsla.smart_sla_tracker.entity.Ticket;
import com.smartsla.smart_sla_tracker.entity.TicketStatus;
import com.smartsla.smart_sla_tracker.repository.TicketRepository;
import com.smartsla.smart_sla_tracker.service.TicketAuditService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final TicketRepository ticketRepository;
    private final TicketAuditService ticketAuditService;

    public AgentController(
            TicketRepository ticketRepository,
            TicketAuditService ticketAuditService
    ) {
        this.ticketRepository = ticketRepository;
        this.ticketAuditService = ticketAuditService;
    }

    // ✅ VIEW MY ASSIGNED TICKETS
    @GetMapping("/tickets")
    public List<Ticket> getMyTickets() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return ticketRepository.findByAssignedAgentUsername(username);
    }

    // ✅ RESOLVE TICKET (SECURE + AUDITED)
    @PutMapping("/tickets/{id}/resolve")
    public String resolveTicket(@PathVariable Long id) {

        String agentUsername = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // 🔐 SECURITY CHECK
        if (ticket.getAssignedAgent() == null ||
                !ticket.getAssignedAgent().getUsername().equals(agentUsername)) {
            throw new RuntimeException("You are not assigned to this ticket");
        }

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setStatus(TicketStatus.RESOLVED);
        ticketRepository.save(ticket);

        // 🔍 AUDIT LOG
        ticketAuditService.log(
                ticket.getId(),
                "RESOLVED_BY_AGENT",
                oldStatus.name(),
                TicketStatus.RESOLVED.name(),
                agentUsername
        );

        return "Ticket resolved successfully";
    }
}



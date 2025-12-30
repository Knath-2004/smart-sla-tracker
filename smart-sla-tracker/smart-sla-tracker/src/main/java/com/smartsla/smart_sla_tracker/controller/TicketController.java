package com.smartsla.smart_sla_tracker.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.smartsla.smart_sla_tracker.entity.Ticket;
import com.smartsla.smart_sla_tracker.entity.TicketStatus;
import com.smartsla.smart_sla_tracker.repository.TicketRepository;
import com.smartsla.smart_sla_tracker.service.SlaCalculatorService;
import com.smartsla.smart_sla_tracker.service.PriorityResolverService;

@RestController
@CrossOrigin(origins = "*")

@RequestMapping("/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final SlaCalculatorService slaCalculatorService;
    private final PriorityResolverService priorityResolverService;

    public TicketController(
            TicketRepository ticketRepository,
            SlaCalculatorService slaCalculatorService,
            PriorityResolverService priorityResolverService) {
        this.ticketRepository = ticketRepository;
        this.slaCalculatorService = slaCalculatorService;
        this.priorityResolverService = priorityResolverService;
    }

    // ✅ CREATE TICKET
    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {

        ticket.setCreatedAt(LocalDateTime.now());

        // Resolve priority from category
        ticket.setPriority(
                priorityResolverService.resolvePriority(ticket.getCategory())
        );

        // Calculate SLA
        ticket.setSlaDeadline(
                slaCalculatorService.calculateSlaDeadline(ticket.getPriority())
        );

        ticket.setStatus(TicketStatus.OPEN);

        Ticket saved = ticketRepository.save(ticket);

        // ✅ CONSOLE CONFIRMATION
        System.out.println(
                "Ticket #" + saved.getId() +
                        " created | Category=" + saved.getCategory() +
                        " | Priority=" + saved.getPriority() +
                        " | SLA=" + saved.getSlaDeadline()
        );

        return saved;
    }
    }
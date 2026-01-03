package com.smartsla.smart_sla_tracker.service;

import com.smartsla.smart_sla_tracker.dto.CreateTicketRequest;
import com.smartsla.smart_sla_tracker.entity.AppUser;
import com.smartsla.smart_sla_tracker.entity.Ticket;
import com.smartsla.smart_sla_tracker.entity.TicketStatus;
import com.smartsla.smart_sla_tracker.repository.TicketRepository;
import com.smartsla.smart_sla_tracker.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketAuditService ticketAuditService;
    private final SlaCalculatorService slaCalculatorService;
    private final PriorityResolverService priorityResolverService;

    public TicketService(
            TicketRepository ticketRepository,
            UserRepository userRepository,
            TicketAuditService ticketAuditService,
            SlaCalculatorService slaCalculatorService,
            PriorityResolverService priorityResolverService) {

        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.ticketAuditService = ticketAuditService;
        this.slaCalculatorService = slaCalculatorService;
        this.priorityResolverService = priorityResolverService;
    }

    /* =========================
       CREATE TICKET
       ========================= */
    public Ticket createTicket(CreateTicketRequest request) {

        // 🔐 1. Logged-in user
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🧠 2. Build entity manually (SAFE)
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setCategory(request.getCategory());

        // 🎯 3. Backend business logic
        ticket.setPriority(
                priorityResolverService.resolvePriority(request.getCategory())
        );

        ticket.setSlaDeadline(
                slaCalculatorService.calculateSlaDeadline(ticket.getPriority())
        );

        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(user);

        // 💾 4. Save
        Ticket savedTicket = ticketRepository.save(ticket);

        // 📝 5. Audit
        ticketAuditService.log(
                savedTicket.getId(),
                "CREATED",
                null,
                "Ticket created",
                username
        );

        return savedTicket;
    }

    public List<Ticket> getTicketsByUser(AppUser user) {
        return ticketRepository.findByCreatedBy(user);
    }

}



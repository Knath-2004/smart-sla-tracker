package com.smartsla.smart_sla_tracker.controller;

import com.smartsla.smart_sla_tracker.dto.TicketResponse;
import com.smartsla.smart_sla_tracker.entity.*;
import com.smartsla.smart_sla_tracker.repository.*;
import com.smartsla.smart_sla_tracker.service.TicketAuditService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TicketRepository ticketRepository;
    private final SlaPolicyRepository slaPolicyRepository;
    private final UserRepository userRepository;
    private final TicketAuditService ticketAuditService;

    public AdminController(
            TicketRepository ticketRepository,
            SlaPolicyRepository slaPolicyRepository,
            UserRepository userRepository,
            TicketAuditService ticketAuditService
    ) {
        this.ticketRepository = ticketRepository;
        this.slaPolicyRepository = slaPolicyRepository;
        this.userRepository = userRepository;
        this.ticketAuditService = ticketAuditService;
    }

    // ================= SLA POLICY =================

    @PostMapping("/sla-policy")
    public ResponseEntity<SlaPolicy> saveSlaPolicy(
            @RequestBody SlaPolicy policy
    ) {
        return ResponseEntity.ok(slaPolicyRepository.save(policy));
    }

    @GetMapping("/sla-policy")
    public ResponseEntity<List<SlaPolicy>> getAllPolicies() {
        return ResponseEntity.ok(slaPolicyRepository.findAll());
    }

    // ================= TICKETS =================

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponse>> getAllTickets() {

        List<TicketResponse> response = ticketRepository.findAll()
                .stream()
                .map(ticket -> new TicketResponse(
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getDescription(),
                        ticket.getStatus(),
                        ticket.isEscalated(),
                        ticket.getCreatedBy() != null
                                ? ticket.getCreatedBy().getUsername()
                                : null,
                        ticket.getAssignedAgent() != null
                                ? ticket.getAssignedAgent().getUsername()
                                : null,
                        ticket.getSlaDeadline()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }


    // ✅ RESOLVE TICKET (AUDITED)
    @PutMapping("/tickets/{id}/resolve")
    public ResponseEntity<String> resolveTicket(@PathVariable Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        String adminUsername = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setEscalated(false);
        ticketRepository.save(ticket);

        // 🔍 AUDIT LOG
        ticketAuditService.log(
                ticket.getId(),
                "RESOLVED_BY_ADMIN",
                oldStatus.name(),
                TicketStatus.RESOLVED.name(),
                adminUsername
        );

        return ResponseEntity.ok("Ticket resolved successfully");
    }

    // ✅ ESCALATE TICKET (AUDITED)
    @PutMapping("/tickets/{id}/escalate")
    public ResponseEntity<String> escalateTicket(@PathVariable Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        String adminUsername = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setStatus(TicketStatus.ESCALATED);
        ticket.setEscalated(true);
        ticketRepository.save(ticket);

        // 🔍 AUDIT LOG
        ticketAuditService.log(
                ticket.getId(),
                "ESCALATED_BY_ADMIN",
                oldStatus.name(),
                TicketStatus.ESCALATED.name(),
                adminUsername
        );

        return ResponseEntity.ok("Ticket escalated manually");
    }

    // ✅ ASSIGN TICKET TO AGENT (AUDITED)
    @PutMapping("/tickets/{ticketId}/assign/{agentId}")
    public ResponseEntity<String> assignTicket(
            @PathVariable Long ticketId,
            @PathVariable Long agentId
    ) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        AppUser agent = userRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (agent.getRole() != Role.AGENT) {
            throw new RuntimeException("User is not an agent");
        }

        String adminUsername = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        String oldAgent = ticket.getAssignedAgent() == null
                ? "UNASSIGNED"
                : ticket.getAssignedAgent().getUsername();

        ticket.setAssignedAgent(agent);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticketRepository.save(ticket);

        // 🔍 AUDIT LOG
        ticketAuditService.log(
                ticket.getId(),
                "ASSIGNED",
                oldAgent,
                agent.getUsername(),
                adminUsername
        );

        return ResponseEntity.ok("Ticket assigned to agent successfully");
    }

    @GetMapping("/users")
    public List<AppUser> getUsersByRole(@RequestParam Role role) {
        return userRepository.findByRole(role);
    }

}



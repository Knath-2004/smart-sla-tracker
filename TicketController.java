package com.smartsla.smart_sla_tracker.controller;

import java.util.List;

import com.smartsla.smart_sla_tracker.dto.CreateTicketRequest;
import com.smartsla.smart_sla_tracker.entity.AppUser;
import com.smartsla.smart_sla_tracker.entity.Ticket;
import com.smartsla.smart_sla_tracker.repository.UserRepository;
import com.smartsla.smart_sla_tracker.service.TicketService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public TicketController(TicketService ticketService,
                            UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    /* =========================
       CREATE TICKET
       ========================= */
    @PostMapping
    public Ticket createTicket(@RequestBody CreateTicketRequest request) {
        return ticketService.createTicket(request);
    }

    /* =========================
       GET MY TICKETS (FIXES SPINNER)
       ========================= */
    @GetMapping
    public List<Ticket> myTickets() {

        // 🔐 get logged-in username
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ticketService.getTicketsByUser(user);
    }
}

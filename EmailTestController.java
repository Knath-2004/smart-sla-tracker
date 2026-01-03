package com.smartsla.smart_sla_tracker.controller;

import com.smartsla.smart_sla_tracker.repository.TicketRepository;
import com.smartsla.smart_sla_tracker.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class EmailTestController {
    @Autowired
    EmailNotificationService emailNotificationService;
    @Autowired
    TicketRepository ticketRepository;
    @GetMapping("/test-mail")
    public String testMail() {
        emailNotificationService.sendSlaBreachEmail(ticketRepository.findById(1L).get());
        return "Mail sent";
    }
}

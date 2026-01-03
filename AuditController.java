package com.smartsla.smart_sla_tracker.controller;



import com.smartsla.smart_sla_tracker.entity.TicketAudit;
import com.smartsla.smart_sla_tracker.repository.TicketAuditRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final TicketAuditRepository auditRepository;

    public AuditController(TicketAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @GetMapping("/ticket/{ticketId}")
    public List<TicketAudit> getTicketAudit(@PathVariable Long ticketId) {
        return auditRepository.findByTicketIdOrderByPerformedAtDesc(ticketId);
    }
}

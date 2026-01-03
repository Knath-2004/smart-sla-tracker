package com.smartsla.smart_sla_tracker.service;



import com.smartsla.smart_sla_tracker.entity.TicketAudit;
import com.smartsla.smart_sla_tracker.repository.TicketAuditRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketAuditService {

    private final TicketAuditRepository auditRepository;

    public TicketAuditService(TicketAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void log(
            Long ticketId,
            String action,
            String oldValue,
            String newValue,
            String performedBy
    ) {
        TicketAudit audit = new TicketAudit();
        audit.setTicketId(ticketId);
        audit.setAction(action);
        audit.setOldValue(oldValue);
        audit.setNewValue(newValue);
        audit.setPerformedBy(performedBy);
        audit.setPerformedAt(LocalDateTime.now());

        auditRepository.save(audit);
    }
}


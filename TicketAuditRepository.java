package com.smartsla.smart_sla_tracker.repository;



import com.smartsla.smart_sla_tracker.entity.TicketAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketAuditRepository extends JpaRepository<TicketAudit, Long> {

    List<TicketAudit> findByTicketIdOrderByPerformedAtDesc(Long ticketId);
}


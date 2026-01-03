package com.smartsla.smart_sla_tracker.repository;

import com.smartsla.smart_sla_tracker.entity.AppUser;
import com.smartsla.smart_sla_tracker.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findBySlaDeadlineBeforeAndEscalatedFalse(LocalDateTime time);

    List<Ticket> findByAssignedAgentUsername(String username);

    long countByEscalatedTrue();

    long countByPriority(com.smartsla.smart_sla_tracker.entity.Priority priority);

    long countByPriorityAndEscalatedTrue(
            com.smartsla.smart_sla_tracker.entity.Priority priority
    );

    long countByAssignedAgentUsername(String username);

    List<Ticket> findByCreatedBy(AppUser user);
}
package com.smartsla.smart_sla_tracker.repository;

import com.smartsla.smart_sla_tracker.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findBySlaDeadlineBeforeAndEscalatedFalse(LocalDateTime time);
}
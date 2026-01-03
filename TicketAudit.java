package com.smartsla.smart_sla_tracker.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_audit")
public class TicketAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ticketId;

    private String action; // CREATED, ASSIGNED, ESCALATED, RESOLVED

    private String oldValue;

    private String newValue;

    private String performedBy;

    private LocalDateTime performedAt;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(LocalDateTime performedAt) {
        this.performedAt = performedAt;
    }
}

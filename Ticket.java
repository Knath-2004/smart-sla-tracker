package com.smartsla.smart_sla_tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =====================
       BASIC DETAILS
       ===================== */

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    /* =====================
       TICKET STATE
       ===================== */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPEN;   // ✅ default

    @Column(nullable = false)
    private boolean escalated = false;

    /* =====================
       SLA & TIMESTAMPS
       ===================== */

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime slaDeadline;

    /* =====================
       RELATIONSHIPS
       ===================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AppUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_agent_id")
    private AppUser assignedAgent;

    /* =====================
       LIFECYCLE CALLBACKS
       ===================== */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /* =====================
       GETTERS & SETTERS
       ===================== */

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueCategory getCategory() {
        return category;
    }

    public void setCategory(IssueCategory category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public boolean isEscalated() {
        return escalated;
    }

    public void setEscalated(boolean escalated) {
        this.escalated = escalated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getSlaDeadline() {
        return slaDeadline;
    }

    public void setSlaDeadline(LocalDateTime slaDeadline) {
        this.slaDeadline = slaDeadline;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public AppUser getAssignedAgent() {
        return assignedAgent;
    }

    public void setAssignedAgent(AppUser assignedAgent) {
        this.assignedAgent = assignedAgent;
    }
}


package com.smartsla.smart_sla_tracker.dto;


import com.smartsla.smart_sla_tracker.entity.TicketStatus;
import java.time.LocalDateTime;

public class TicketResponse {

    public Long id;
    public String title;
    public String description;
    public TicketStatus status;
    public boolean escalated;
    public String createdBy;
    public String assignedAgent;
    public LocalDateTime slaDeadline;

    public TicketResponse(
            Long id,
            String title,
            String description,
            TicketStatus status,
            boolean escalated,
            String createdBy,
            String assignedAgent,
            LocalDateTime slaDeadline
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.escalated = escalated;
        this.createdBy = createdBy;
        this.assignedAgent = assignedAgent;
        this.slaDeadline = slaDeadline;
    }
}


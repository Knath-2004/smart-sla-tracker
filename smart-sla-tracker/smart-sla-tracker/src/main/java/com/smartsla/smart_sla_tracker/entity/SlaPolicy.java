package com.smartsla.smart_sla_tracker.entity;

import jakarta.persistence.*;

@Entity
public class SlaPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    // SLA time in hours
    private Integer resolutionTimeHours;

    // getters and setters
    public Long getId() {
        return id;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Integer getResolutionTimeHours() {
        return resolutionTimeHours;
    }

    public void setResolutionTimeHours(Integer resolutionTimeHours) {
        this.resolutionTimeHours = resolutionTimeHours;
    }
}

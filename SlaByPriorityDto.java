package com.smartsla.smart_sla_tracker.dto;



import com.smartsla.smart_sla_tracker.entity.Priority;

public class SlaByPriorityDto {

    private Priority priority;
    private long total;
    private long breached;

    public SlaByPriorityDto(Priority priority, long total, long breached) {
        this.priority = priority;
        this.total = total;
        this.breached = breached;
    }

    public Priority getPriority() {
        return priority;
    }

    public long getTotal() {
        return total;
    }

    public long getBreached() {
        return breached;
    }
}


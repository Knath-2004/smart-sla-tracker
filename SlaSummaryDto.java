package com.smartsla.smart_sla_tracker.dto;



public class SlaSummaryDto {

    private long totalTickets;
    private long breachedTickets;
    private double breachPercentage;

    public SlaSummaryDto(long totalTickets, long breachedTickets) {
        this.totalTickets = totalTickets;
        this.breachedTickets = breachedTickets;
        this.breachPercentage =
                totalTickets == 0 ? 0 :
                        (breachedTickets * 100.0) / totalTickets;
    }

    public long getTotalTickets() {
        return totalTickets;
    }

    public long getBreachedTickets() {
        return breachedTickets;
    }

    public double getBreachPercentage() {
        return breachPercentage;
    }
}


package com.smartsla.smart_sla_tracker.service;

import org.springframework.stereotype.Service;
import com.smartsla.smart_sla_tracker.entity.IssueCategory;
import com.smartsla.smart_sla_tracker.entity.Priority;

@Service
public class PriorityResolverService {

    public Priority resolvePriority(IssueCategory category) {

        return switch (category) {
            case PAYMENT_ISSUE, NETWORK_ISSUE -> Priority.HIGH;
            case LOGIN_ISSUE -> Priority.MEDIUM;
            case GENERAL_QUERY -> Priority.LOW;
        };
    }
}

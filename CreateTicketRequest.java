package com.smartsla.smart_sla_tracker.dto;

import com.smartsla.smart_sla_tracker.entity.IssueCategory;

public class CreateTicketRequest {

    private String title;
    private String description;
    private IssueCategory category;

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
}



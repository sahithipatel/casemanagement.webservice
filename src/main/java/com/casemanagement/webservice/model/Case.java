package com.casemanagement.webservice.model;

import java.time.LocalDate;

public class Case {
    private String caseId;
    private String title;
    private String description;
    private String status;
    private String assignedTo;
    private LocalDate createdDate;

    public Case() {
    }

    public Case(String caseId, String title, String description, String status,
                String assignedTo, LocalDate createdDate) {
        this.caseId = caseId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignedTo = assignedTo;
        this.createdDate = createdDate;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}

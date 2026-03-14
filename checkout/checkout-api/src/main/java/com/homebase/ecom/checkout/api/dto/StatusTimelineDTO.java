package com.homebase.ecom.checkout.api.dto;

import java.time.LocalDateTime;

/**
 * Status Timeline DTO
 */
public class StatusTimelineDTO {

    private String status;
    private String description;
    private LocalDateTime timestamp;

    public StatusTimelineDTO() {
    }

    public StatusTimelineDTO(String status, String description, LocalDateTime timestamp) {
        this.status = status;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

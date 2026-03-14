package com.homebase.ecom.checkout.api.dto;

import java.util.List;

/**
 * Compensation Response DTO
 */
public class CompensationResponseDTO {

    private String status;
    private List<String> compensatedSteps;
    private String reason;

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getCompensatedSteps() {
        return compensatedSteps;
    }

    public void setCompensatedSteps(List<String> compensatedSteps) {
        this.compensatedSteps = compensatedSteps;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

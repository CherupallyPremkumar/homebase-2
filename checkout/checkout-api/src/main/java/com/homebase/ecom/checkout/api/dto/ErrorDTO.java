package com.homebase.ecom.checkout.api.dto;

import java.util.Map;

/**
 * Error DTO
 */
public class ErrorDTO {

    private String code;
    private String message;
    private Map<String, Object> details;

    public ErrorDTO() {
    }

    public ErrorDTO(String code, String message, Map<String, Object> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}

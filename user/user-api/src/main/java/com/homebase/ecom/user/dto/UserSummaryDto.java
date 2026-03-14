package com.homebase.ecom.user.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A custom DTO for user summary information.
 * Used to demonstrate the framework's DTO mapping capabilities.
 */
public class UserSummaryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String email;
    private String role;
    private List<Map<String, String>> allowedActions;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Map<String, String>> getAllowedActions() {
        return allowedActions;
    }

    public void setAllowedActions(List<Map<String, String>> allowedActions) {
        this.allowedActions = allowedActions;
    }

    @Override
    public String toString() {
        return "UserSummaryDto [userId=" + userId + ", email=" + email + ", role=" + role + "]";
    }
}

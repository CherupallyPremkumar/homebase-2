package com.homebase.ecom.user.dto;

import java.time.Instant;
import java.util.List;

/**
 * DTO for User profile data.
 * Lives in user-api — safe to share across all modules.
 * NO domain logic, NO JPA, NO Chenile STM classes.
 */
public class UserDto {

    private String id;
    private String keycloakId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String avatarUrl;
    private String status;          // current STM state: PENDING_VERIFICATION, ACTIVE, etc.
    private List<String> allowedActions; // STM-provided next events
    private String currency;        // from Preferences
    private String language;        // from Preferences
    private Instant createdAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getKeycloakId() { return keycloakId; }
    public void setKeycloakId(String keycloakId) { this.keycloakId = keycloakId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getAllowedActions() { return allowedActions; }
    public void setAllowedActions(List<String> allowedActions) { this.allowedActions = allowedActions; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

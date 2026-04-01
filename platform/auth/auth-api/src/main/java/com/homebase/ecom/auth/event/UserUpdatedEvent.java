package com.homebase.ecom.auth.event;

/**
 * Published when a user's profile or roles change in Keycloak.
 */
public class UserUpdatedEvent {

    public static final String EVENT_TYPE = "USER_UPDATED";

    private String keycloakUserId;
    private String email;
    private String updateType;   // ROLE_CHANGE, PROFILE_UPDATE, PASSWORD_CHANGE, DISABLED, ENABLED
    private String realm;
    private long timestamp;

    public String getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(String v) { this.keycloakUserId = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getUpdateType() { return updateType; }
    public void setUpdateType(String v) { this.updateType = v; }
    public String getRealm() { return realm; }
    public void setRealm(String v) { this.realm = v; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long v) { this.timestamp = v; }
}

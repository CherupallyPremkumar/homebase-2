package com.homebase.ecom.auth.event;

import java.util.List;

/**
 * Published when a user registers in Keycloak.
 * User module consumes this to create profile in DB.
 */
public class UserRegisteredEvent {

    public static final String EVENT_TYPE = "USER_REGISTERED";

    private String keycloakUserId;
    private String email;
    private String firstName;
    private String lastName;
    private String realm;
    private List<String> roles;
    private long timestamp;

    public String getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(String v) { this.keycloakUserId = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String v) { this.firstName = v; }
    public String getLastName() { return lastName; }
    public void setLastName(String v) { this.lastName = v; }
    public String getRealm() { return realm; }
    public void setRealm(String v) { this.realm = v; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> v) { this.roles = v; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long v) { this.timestamp = v; }
}

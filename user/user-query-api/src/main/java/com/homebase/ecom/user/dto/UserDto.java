package com.homebase.ecom.user.dto;

/**
 * Canonical response DTO for the "users" and "user" queries.
 *
 * Field names MUST match the SQL column aliases defined in user.xml.
 * Consumer services may define their own DTOs but field names must align.
 */
public class UserDto {

    private String id;
    private String userId;    // alias for id — used as filter key
    private String name;
    private String firstName; // alias for name
    private String lastName;  // alias for name
    private String email;
    private String phone;
    private String branch;
    private String role;      // alias for branch
    private String percentage;
    private String stateId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPercentage() { return percentage; }
    public void setPercentage(String percentage) { this.percentage = percentage; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
}

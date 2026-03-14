package com.homebase.ecom.supplier.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for Supplier query responses.
 */
public class SupplierDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private String email;
    private String status;
    private String stateId;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

package com.homebase.ecom.compliance.model;

import java.time.Instant;

/**
 * Non-STM plain entity representing a regulation.
 * No JPA annotations — domain purity.
 */
public class Regulation {
    private String id;
    private Instant createdTime;
    private Instant lastModifiedTime;
    private long version;
    private String name;
    private String regulationType;
    private String jurisdiction;
    private String description;
    private String referenceUrl;
    private boolean active;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Instant getCreatedTime() { return createdTime; }
    public void setCreatedTime(Instant createdTime) { this.createdTime = createdTime; }
    public Instant getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(Instant lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
    public long getVersion() { return version; }
    public void setVersion(long version) { this.version = version; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegulationType() { return regulationType; }
    public void setRegulationType(String regulationType) { this.regulationType = regulationType; }
    public String getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(String jurisdiction) { this.jurisdiction = jurisdiction; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReferenceUrl() { return referenceUrl; }
    public void setReferenceUrl(String referenceUrl) { this.referenceUrl = referenceUrl; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

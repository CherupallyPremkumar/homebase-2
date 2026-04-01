package com.homebase.ecom.compliance.model;

import java.time.Instant;

public class ComplianceAudit {
    private String id;
    private String regulationId;
    private String auditType;
    private String status;
    private String findings;
    private String auditorId;
    private Instant auditDate;
    private Instant nextAuditDate;
    private String tenant;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRegulationId() { return regulationId; }
    public void setRegulationId(String regulationId) { this.regulationId = regulationId; }
    public String getAuditType() { return auditType; }
    public void setAuditType(String auditType) { this.auditType = auditType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFindings() { return findings; }
    public void setFindings(String findings) { this.findings = findings; }
    public String getAuditorId() { return auditorId; }
    public void setAuditorId(String auditorId) { this.auditorId = auditorId; }
    public Instant getAuditDate() { return auditDate; }
    public void setAuditDate(Instant auditDate) { this.auditDate = auditDate; }
    public Instant getNextAuditDate() { return nextAuditDate; }
    public void setNextAuditDate(Instant nextAuditDate) { this.nextAuditDate = nextAuditDate; }
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}

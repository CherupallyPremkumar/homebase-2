package com.homebase.ecom.compliance.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import java.time.Instant;

@Entity
@Table(name = "compliance_audits")
public class ComplianceAuditEntity extends BaseJpaEntity {

    @Column(name = "regulation_id")
    private String regulationId;

    @Column(name = "audit_type")
    private String auditType;

    @Column(name = "status")
    private String status;

    @Column(name = "findings", length = 4000)
    private String findings;

    @Column(name = "auditor_id")
    private String auditorId;

    @Column(name = "audit_date")
    private Instant auditDate;

    @Column(name = "next_audit_date")
    private Instant nextAuditDate;

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
}

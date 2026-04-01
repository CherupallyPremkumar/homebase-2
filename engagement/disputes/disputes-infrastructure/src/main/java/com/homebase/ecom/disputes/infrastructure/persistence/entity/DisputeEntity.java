package com.homebase.ecom.disputes.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "dispute")
public class DisputeEntity extends AbstractJpaStateEntity {

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "dispute_type", length = 100)
    private String disputeType;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Column(name = "disputed_amount")
    private BigDecimal disputedAmount;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "customer_complaint", length = 4000)
    private String customerComplaint;

    @Column(name = "seller_response", length = 4000)
    private String sellerResponse;

    @Column(name = "evidence_links", length = 4000)
    private String evidenceLinks;

    @Column(name = "investigator_id")
    private String investigatorId;

    @Column(name = "investigation_notes", length = 4000)
    private String investigationNotes;

    @Column(name = "resolution_outcome", length = 100)
    private String resolutionOutcome;

    @Column(name = "resolution_notes", length = 4000)
    private String resolutionNotes;

    @Column(name = "resolution_date")
    private LocalDateTime resolutionDate;

    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    @Column(name = "sla_target_days")
    private int slaTargetDays = 5;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "dispute_id")
    private List<DisputeActivityLogEntity> activities = new ArrayList<>();

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getDisputeType() { return disputeType; }
    public void setDisputeType(String disputeType) { this.disputeType = disputeType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public BigDecimal getDisputedAmount() { return disputedAmount; }
    public void setDisputedAmount(BigDecimal disputedAmount) { this.disputedAmount = disputedAmount; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getCustomerComplaint() { return customerComplaint; }
    public void setCustomerComplaint(String customerComplaint) { this.customerComplaint = customerComplaint; }

    public String getSellerResponse() { return sellerResponse; }
    public void setSellerResponse(String sellerResponse) { this.sellerResponse = sellerResponse; }

    public String getEvidenceLinks() { return evidenceLinks; }
    public void setEvidenceLinks(String evidenceLinks) { this.evidenceLinks = evidenceLinks; }

    public String getInvestigatorId() { return investigatorId; }
    public void setInvestigatorId(String investigatorId) { this.investigatorId = investigatorId; }

    public String getInvestigationNotes() { return investigationNotes; }
    public void setInvestigationNotes(String investigationNotes) { this.investigationNotes = investigationNotes; }

    public String getResolutionOutcome() { return resolutionOutcome; }
    public void setResolutionOutcome(String resolutionOutcome) { this.resolutionOutcome = resolutionOutcome; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public LocalDateTime getResolutionDate() { return resolutionDate; }
    public void setResolutionDate(LocalDateTime resolutionDate) { this.resolutionDate = resolutionDate; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public int getSlaTargetDays() { return slaTargetDays; }
    public void setSlaTargetDays(int slaTargetDays) { this.slaTargetDays = slaTargetDays; }

    public List<DisputeActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<DisputeActivityLogEntity> activities) { this.activities = activities; }
}

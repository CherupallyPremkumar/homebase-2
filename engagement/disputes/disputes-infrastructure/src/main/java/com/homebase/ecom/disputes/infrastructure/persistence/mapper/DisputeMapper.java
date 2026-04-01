package com.homebase.ecom.disputes.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.homebase.ecom.disputes.model.Dispute;
import com.homebase.ecom.disputes.model.DisputeActivityLog;
import com.homebase.ecom.disputes.infrastructure.persistence.entity.DisputeActivityLogEntity;
import com.homebase.ecom.disputes.infrastructure.persistence.entity.DisputeEntity;
import org.chenile.workflow.activities.model.ActivityLog;

public class DisputeMapper {

    public Dispute toModel(DisputeEntity entity) {
        if (entity == null) return null;
        Dispute model = new Dispute();

        // Base entity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setCreatedBy(entity.getCreatedBy());
        model.setVersion(entity.getVersion() != null ? entity.getVersion() : 0L);

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Tenant
        model.setTenant(entity.tenant);

        // Domain fields
        model.orderId = entity.getOrderId();
        model.customerId = entity.getCustomerId();
        model.sellerId = entity.getSellerId();
        model.disputeType = entity.getDisputeType();
        model.reason = entity.getReason();
        model.disputedAmount = entity.getDisputedAmount();
        model.priority = entity.getPriority();
        model.assignedTo = entity.getAssignedTo();
        model.customerComplaint = entity.getCustomerComplaint();
        model.sellerResponse = entity.getSellerResponse();
        model.evidenceLinks = entity.getEvidenceLinks();
        model.investigatorId = entity.getInvestigatorId();
        model.investigationNotes = entity.getInvestigationNotes();
        model.resolutionOutcome = entity.getResolutionOutcome();
        model.resolutionNotes = entity.getResolutionNotes();
        model.resolutionDate = entity.getResolutionDate();
        model.refundAmount = entity.getRefundAmount();
        model.slaTargetDays = entity.getSlaTargetDays();

        // Activities
        if (entity.getActivities() != null) {
            List<ActivityLog> activityLogs = new ArrayList<>();
            for (DisputeActivityLogEntity actEntity : entity.getActivities()) {
                DisputeActivityLog actLog = new DisputeActivityLog();
                actLog.activityName = actEntity.getName();
                actLog.activityComment = actEntity.getComment();
                actLog.activitySuccess = actEntity.getSuccess();
                activityLogs.add(actLog);
            }
            model.setActivities(activityLogs);
        }

        return model;
    }

    public DisputeEntity toEntity(Dispute model) {
        if (model == null) return null;
        DisputeEntity entity = new DisputeEntity();

        // Base entity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Tenant
        entity.tenant = model.getTenant();

        // Domain fields
        entity.setOrderId(model.orderId);
        entity.setCustomerId(model.customerId);
        entity.setSellerId(model.sellerId);
        entity.setDisputeType(model.disputeType);
        entity.setReason(model.reason);
        entity.setDisputedAmount(model.disputedAmount);
        entity.setPriority(model.priority);
        entity.setAssignedTo(model.assignedTo);
        entity.setCustomerComplaint(model.customerComplaint);
        entity.setSellerResponse(model.sellerResponse);
        entity.setEvidenceLinks(model.evidenceLinks);
        entity.setInvestigatorId(model.investigatorId);
        entity.setInvestigationNotes(model.investigationNotes);
        entity.setResolutionOutcome(model.resolutionOutcome);
        entity.setResolutionNotes(model.resolutionNotes);
        entity.setResolutionDate(model.resolutionDate);
        entity.setRefundAmount(model.refundAmount);
        entity.setSlaTargetDays(model.slaTargetDays);

        // Activities
        if (model.obtainActivities() != null) {
            entity.setActivities(
                model.obtainActivities().stream()
                    .map(this::toActivityEntity)
                    .collect(Collectors.toList())
            );
        }

        return entity;
    }

    /**
     * Merges updated fields from a new entity into an existing managed JPA entity.
     * Used by ChenileJpaEntityStore to preserve optimistic locking via @Version.
     */
    public void mergeEntity(DisputeEntity existing, DisputeEntity updated) {
        // STM state (critical)
        existing.setCurrentState(updated.getCurrentState());
        existing.setStateEntryTime(updated.getStateEntryTime());
        existing.setSlaTendingLate(updated.getSlaTendingLate());
        existing.setSlaLate(updated.getSlaLate());

        // Domain fields
        existing.setOrderId(updated.getOrderId());
        existing.setCustomerId(updated.getCustomerId());
        existing.setSellerId(updated.getSellerId());
        existing.setDisputeType(updated.getDisputeType());
        existing.setReason(updated.getReason());
        existing.setDisputedAmount(updated.getDisputedAmount());
        existing.setPriority(updated.getPriority());
        existing.setAssignedTo(updated.getAssignedTo());
        existing.setCustomerComplaint(updated.getCustomerComplaint());
        existing.setSellerResponse(updated.getSellerResponse());
        existing.setEvidenceLinks(updated.getEvidenceLinks());
        existing.setInvestigatorId(updated.getInvestigatorId());
        existing.setInvestigationNotes(updated.getInvestigationNotes());
        existing.setResolutionOutcome(updated.getResolutionOutcome());
        existing.setResolutionNotes(updated.getResolutionNotes());
        existing.setResolutionDate(updated.getResolutionDate());
        existing.setRefundAmount(updated.getRefundAmount());
        existing.setSlaTargetDays(updated.getSlaTargetDays());
        existing.tenant = updated.tenant;

        // Audit
        existing.setLastModifiedBy(updated.getLastModifiedBy());
        existing.setLastModifiedTime(updated.getLastModifiedTime());

        // Activities -- replace collection contents
        existing.getActivities().clear();
        if (updated.getActivities() != null) {
            existing.getActivities().addAll(updated.getActivities());
        }
    }

    private DisputeActivityLogEntity toActivityEntity(ActivityLog activityLog) {
        DisputeActivityLogEntity entity = new DisputeActivityLogEntity();
        entity.setEventId(activityLog.getName());
        entity.setComment(activityLog.getComment());
        entity.setSuccess(activityLog.getSuccess());
        return entity;
    }
}

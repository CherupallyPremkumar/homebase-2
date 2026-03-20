package com.homebase.ecom.support.infrastructure.persistence;

import com.homebase.ecom.support.infrastructure.persistence.adapter.SupportTicketJpaRepository;
import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketActivityLogEntity;
import com.homebase.ecom.support.infrastructure.persistence.entity.SupportTicketEntity;
import com.homebase.ecom.support.infrastructure.persistence.entity.TicketMessageEntity;
import com.homebase.ecom.support.infrastructure.persistence.mapper.SupportTicketMapper;
import com.homebase.ecom.support.model.SupportTicket;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

import java.util.HashSet;
import java.util.Set;

/**
 * JPA-backed EntityStore for SupportTicket.
 * Uses a merge function to properly update managed child collections (messages, activities)
 * without optimistic locking failures on child entities.
 */
public class ChenileSupportTicketEntityStore extends ChenileJpaEntityStore<SupportTicket, SupportTicketEntity> {

    public ChenileSupportTicketEntityStore(SupportTicketJpaRepository repository, SupportTicketMapper mapper) {
        super(repository,
              entity -> mapper.toModel(entity),
              model -> mapper.toEntity(model),
              (existing, incoming) -> mergeEntities(existing, incoming));
    }

    /**
     * Merges fields from incoming (freshly built from domain model) onto existing (JPA-managed).
     * For child collections (messages, activities), preserves already-persisted entities
     * and only adds new ones, avoiding optimistic locking failures.
     */
    private static void mergeEntities(SupportTicketEntity existing, SupportTicketEntity incoming) {
        // STM state
        existing.setCurrentState(incoming.getCurrentState());
        existing.setStateEntryTime(incoming.getStateEntryTime());

        // Base entity fields
        existing.setLastModifiedTime(incoming.getLastModifiedTime());
        existing.setLastModifiedBy(incoming.getLastModifiedBy());
        existing.setCreatedBy(incoming.getCreatedBy());
        if (existing.getCreatedTime() == null) {
            existing.setCreatedTime(incoming.getCreatedTime());
        }
        existing.tenant = incoming.tenant;

        // Business fields
        existing.setCustomerId(incoming.getCustomerId());
        existing.setOrderId(incoming.getOrderId());
        existing.setSubject(incoming.getSubject());
        existing.setCategory(incoming.getCategory());
        existing.setPriority(incoming.getPriority());
        existing.setDescription(incoming.getDescription());
        existing.setAssignedAgentId(incoming.getAssignedAgentId());
        existing.setResolvedAt(incoming.getResolvedAt());
        existing.setReopenCount(incoming.getReopenCount());
        existing.setSlaBreached(incoming.isSlaBreached());
        existing.setAutoCloseReady(incoming.isAutoCloseReady());

        // Changeset support-004 fields
        existing.setChannel(incoming.getChannel());
        existing.setRelatedEntityType(incoming.getRelatedEntityType());
        existing.setRelatedEntityId(incoming.getRelatedEntityId());
        existing.setSatisfactionRating(incoming.getSatisfactionRating());
        existing.setResolutionNotes(incoming.getResolutionNotes());
        existing.setEscalated(incoming.isEscalated());
        existing.setEscalationReason(incoming.getEscalationReason());

        // Messages: keep existing managed entities, add only new ones
        if (incoming.getMessages() != null) {
            Set<String> existingMsgIds = new HashSet<>();
            for (TicketMessageEntity msg : existing.getMessages()) {
                existingMsgIds.add(msg.getId());
            }
            for (TicketMessageEntity msg : incoming.getMessages()) {
                if (!existingMsgIds.contains(msg.getId())) {
                    existing.getMessages().add(msg);
                }
            }
        }

        // Activities: keep existing managed entities, add only new ones
        if (incoming.getActivities() != null) {
            Set<String> existingActIds = new HashSet<>();
            for (SupportTicketActivityLogEntity act : existing.getActivities()) {
                existingActIds.add(act.getId());
            }
            for (SupportTicketActivityLogEntity act : incoming.getActivities()) {
                if (act.getId() == null || !existingActIds.contains(act.getId())) {
                    existing.getActivities().add(act);
                }
            }
        }
    }
}

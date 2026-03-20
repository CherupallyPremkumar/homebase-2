package com.homebase.ecom.offer.infrastructure.persistence.mapper;

import com.homebase.ecom.offer.api.dto.OfferDTO;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.model.OfferActivityLog;
import com.homebase.ecom.offer.domain.model.OfferType;
import com.homebase.ecom.offer.infrastructure.persistence.entity.OfferEntity;
import com.homebase.ecom.offer.infrastructure.persistence.entity.OfferActivityLogEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Bidirectional mapper: DTO <-> Domain Model <-> JPA Entity.
 * Maps ALL columns from the offer table (offer-001 + offer-003 migrations).
 */
public class OfferMapper {

    // ── DTO -> Domain Model ─────────────────────────────────────────────────

    public Offer toModel(OfferDTO dto) {
        if (dto == null) return null;
        Offer model = new Offer();
        model.setId(dto.getId());
        model.setProductId(dto.getProductId());
        model.setSupplierId(dto.getSupplierId());
        if (dto.getOfferType() != null) {
            model.setOfferType(OfferType.valueOf(dto.getOfferType()));
        }
        model.setTitle(dto.getTitle());
        model.setDescription(dto.getDescription());
        model.setOriginalPrice(dto.getOriginalPrice());
        model.setOfferPrice(dto.getOfferPrice());
        model.setDiscountPercent(dto.getDiscountPercent());
        model.setStartDate(dto.getStartDate());
        model.setEndDate(dto.getEndDate());
        model.setMaxQuantity(dto.getMaxQuantity());
        model.setSoldQuantity(dto.getSoldQuantity());
        model.setSellerRating(dto.getSellerRating());
        model.setBuyBoxWinner(dto.isBuyBoxWinner());
        model.setFulfillmentType(dto.getFulfillmentType());
        model.setShippingCost(dto.getShippingCost());
        model.setDeliveryDays(dto.getDeliveryDays());
        model.setSku(dto.getSku());
        model.setVariantId(dto.getVariantId());
        model.setMrp(dto.getMrp());
        model.setStockQuantity(dto.getStockQuantity());
        model.setMinOrderQty(dto.getMinOrderQty());
        model.setMaxOrderQty(dto.getMaxOrderQty());
        model.setItemCondition(dto.getItemCondition());
        model.setShippingDaysMin(dto.getShippingDaysMin());
        model.setShippingDaysMax(dto.getShippingDaysMax());
        model.setBuyBoxScore(dto.getBuyBoxScore());
        model.setActive(dto.isActive());
        model.setCurrency(dto.getCurrency());
        return model;
    }

    // ── Domain Model -> DTO ─────────────────────────────────────────────────

    public OfferDTO toDTO(Offer model) {
        if (model == null) return null;
        OfferDTO dto = new OfferDTO();
        dto.setId(model.getId());
        dto.setProductId(model.getProductId());
        dto.setSupplierId(model.getSupplierId());
        dto.setOfferType(model.getOfferType() != null ? model.getOfferType().name() : null);
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setOriginalPrice(model.getOriginalPrice());
        dto.setOfferPrice(model.getOfferPrice());
        dto.setDiscountPercent(model.getDiscountPercent());
        dto.setStartDate(model.getStartDate());
        dto.setEndDate(model.getEndDate());
        dto.setMaxQuantity(model.getMaxQuantity());
        dto.setSoldQuantity(model.getSoldQuantity());
        dto.setSellerRating(model.getSellerRating());
        dto.setBuyBoxWinner(model.isBuyBoxWinner());
        dto.setFulfillmentType(model.getFulfillmentType());
        dto.setShippingCost(model.getShippingCost());
        dto.setDeliveryDays(model.getDeliveryDays());
        dto.setSku(model.getSku());
        dto.setVariantId(model.getVariantId());
        dto.setMrp(model.getMrp());
        dto.setStockQuantity(model.getStockQuantity());
        dto.setMinOrderQty(model.getMinOrderQty());
        dto.setMaxOrderQty(model.getMaxOrderQty());
        dto.setItemCondition(model.getItemCondition());
        dto.setShippingDaysMin(model.getShippingDaysMin());
        dto.setShippingDaysMax(model.getShippingDaysMax());
        dto.setBuyBoxScore(model.getBuyBoxScore());
        dto.setActive(model.isActive());
        dto.setCurrency(model.getCurrency());
        dto.setStatus(model.getCurrentState() != null ? model.getCurrentState().getStateId() : null);
        return dto;
    }

    // ── JPA Entity -> Domain Model ──────────────────────────────────────────

    public Offer toModel(OfferEntity entity) {
        if (entity == null) return null;
        Offer model = new Offer();
        // BaseEntity fields
        model.setId(entity.getId());
        model.setVersion(entity.getVersion());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setCreatedBy(entity.getCreatedBy());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        // STM fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setTenant(entity.tenant);
        // Business fields (offer-001)
        model.setProductId(entity.getProductId());
        model.setSupplierId(entity.getSupplierId());
        if (entity.getOfferType() != null) {
            model.setOfferType(OfferType.valueOf(entity.getOfferType()));
        }
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setOriginalPrice(entity.getOriginalPrice());
        model.setOfferPrice(entity.getOfferPrice());
        model.setDiscountPercent(entity.getDiscountPercent());
        model.setStartDate(entity.getStartDate());
        model.setEndDate(entity.getEndDate());
        model.setMaxQuantity(entity.getMaxQuantity());
        model.setSoldQuantity(entity.getSoldQuantity());
        model.setSellerRating(entity.getSellerRating());
        model.setBuyBoxWinner(entity.isBuyBoxWinner());
        model.setFulfillmentType(entity.getFulfillmentType());
        model.setShippingCost(entity.getShippingCost());
        model.setDeliveryDays(entity.getDeliveryDays());
        // Business fields (offer-003)
        model.setSku(entity.getSku());
        model.setVariantId(entity.getVariantId());
        model.setMrp(entity.getMrp());
        model.setStockQuantity(entity.getStockQuantity());
        model.setMinOrderQty(entity.getMinOrderQty());
        model.setMaxOrderQty(entity.getMaxOrderQty());
        model.setItemCondition(entity.getItemCondition());
        model.setShippingDaysMin(entity.getShippingDaysMin());
        model.setShippingDaysMax(entity.getShippingDaysMax());
        model.setBuyBoxScore(entity.getBuyBoxScore());
        model.setActive(entity.isActive());
        model.setCurrency(entity.getCurrency());

        if (entity.getActivities() != null) {
            model.setActivities(entity.getActivities().stream()
                .map(this::toActivityLogModel)
                .collect(Collectors.toCollection(ArrayList::new)));
        }
        return model;
    }

    // ── Domain Model -> JPA Entity ──────────────────────────────────────────

    public OfferEntity toEntity(Offer model) {
        if (model == null) return null;
        OfferEntity entity = new OfferEntity();
        // BaseEntity fields
        entity.setId(model.getId());
        if (model.getVersion() != null) {
            entity.setVersion(model.getVersion());
        }
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        // STM fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.tenant = model.getTenant();
        // Business fields (offer-001)
        entity.setProductId(model.getProductId());
        entity.setSupplierId(model.getSupplierId());
        entity.setOfferType(model.getOfferType() != null ? model.getOfferType().name() : null);
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setOriginalPrice(model.getOriginalPrice());
        entity.setOfferPrice(model.getOfferPrice());
        entity.setDiscountPercent(model.getDiscountPercent());
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        entity.setMaxQuantity(model.getMaxQuantity());
        entity.setSoldQuantity(model.getSoldQuantity());
        entity.setSellerRating(model.getSellerRating());
        entity.setBuyBoxWinner(model.isBuyBoxWinner());
        entity.setFulfillmentType(model.getFulfillmentType());
        entity.setShippingCost(model.getShippingCost());
        entity.setDeliveryDays(model.getDeliveryDays());
        // Business fields (offer-003)
        entity.setSku(model.getSku());
        entity.setVariantId(model.getVariantId());
        entity.setMrp(model.getMrp());
        entity.setStockQuantity(model.getStockQuantity());
        entity.setMinOrderQty(model.getMinOrderQty());
        entity.setMaxOrderQty(model.getMaxOrderQty());
        entity.setItemCondition(model.getItemCondition());
        entity.setShippingDaysMin(model.getShippingDaysMin());
        entity.setShippingDaysMax(model.getShippingDaysMax());
        entity.setBuyBoxScore(model.getBuyBoxScore());
        entity.setActive(model.isActive());
        entity.setCurrency(model.getCurrency());

        if (model.getActivities() != null) {
            entity.setActivities(model.getActivities().stream()
                .map(this::toActivityLogEntity)
                .collect(Collectors.toCollection(ArrayList::new)));
        }
        return entity;
    }

    // ── Merge: copies mutable fields from incoming model onto existing entity ──

    /**
     * Merges incoming domain model fields onto an existing JPA entity.
     * Preserves the existing entity's id, version, createdTime, createdBy,
     * currentState, and activities (managed by STM).
     * Only updates business-mutable fields that are non-null in the incoming model.
     */
    public OfferEntity mergeEntity(Offer incoming, OfferEntity existing) {
        if (incoming == null || existing == null) return existing;

        // Business fields -- always overwrite from incoming (STM controls what's allowed)
        if (incoming.getProductId() != null) existing.setProductId(incoming.getProductId());
        if (incoming.getSupplierId() != null) existing.setSupplierId(incoming.getSupplierId());
        if (incoming.getOfferType() != null) existing.setOfferType(incoming.getOfferType().name());
        if (incoming.getTitle() != null) existing.setTitle(incoming.getTitle());
        if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
        if (incoming.getOriginalPrice() != null) existing.setOriginalPrice(incoming.getOriginalPrice());
        if (incoming.getOfferPrice() != null) existing.setOfferPrice(incoming.getOfferPrice());
        if (incoming.getDiscountPercent() != null) existing.setDiscountPercent(incoming.getDiscountPercent());
        if (incoming.getStartDate() != null) existing.setStartDate(incoming.getStartDate());
        if (incoming.getEndDate() != null) existing.setEndDate(incoming.getEndDate());
        existing.setMaxQuantity(incoming.getMaxQuantity());
        existing.setSoldQuantity(incoming.getSoldQuantity());
        if (incoming.getSellerRating() != null) existing.setSellerRating(incoming.getSellerRating());
        existing.setBuyBoxWinner(incoming.isBuyBoxWinner());
        if (incoming.getFulfillmentType() != null) existing.setFulfillmentType(incoming.getFulfillmentType());
        if (incoming.getShippingCost() != null) existing.setShippingCost(incoming.getShippingCost());
        if (incoming.getDeliveryDays() != null) existing.setDeliveryDays(incoming.getDeliveryDays());
        if (incoming.getSku() != null) existing.setSku(incoming.getSku());
        if (incoming.getVariantId() != null) existing.setVariantId(incoming.getVariantId());
        if (incoming.getMrp() != null) existing.setMrp(incoming.getMrp());
        existing.setStockQuantity(incoming.getStockQuantity());
        existing.setMinOrderQty(incoming.getMinOrderQty());
        existing.setMaxOrderQty(incoming.getMaxOrderQty());
        if (incoming.getItemCondition() != null) existing.setItemCondition(incoming.getItemCondition());
        if (incoming.getShippingDaysMin() != null) existing.setShippingDaysMin(incoming.getShippingDaysMin());
        if (incoming.getShippingDaysMax() != null) existing.setShippingDaysMax(incoming.getShippingDaysMax());
        if (incoming.getBuyBoxScore() != null) existing.setBuyBoxScore(incoming.getBuyBoxScore());
        existing.setActive(incoming.isActive());
        if (incoming.getCurrency() != null) existing.setCurrency(incoming.getCurrency());

        // STM state -- always copy from incoming (STM has already computed the new state)
        existing.setCurrentState(incoming.getCurrentState());
        existing.setStateEntryTime(incoming.getStateEntryTime());
        existing.setLastModifiedTime(incoming.getLastModifiedTime());
        existing.setLastModifiedBy(incoming.getLastModifiedBy());
        if (incoming.getTenant() != null) existing.tenant = incoming.getTenant();

        // Activities -- copy from incoming (STM may have added new activities)
        if (incoming.getActivities() != null) {
            existing.getActivities().clear();
            existing.getActivities().addAll(
                incoming.getActivities().stream()
                    .map(this::toActivityLogEntity)
                    .collect(Collectors.toCollection(ArrayList::new))
            );
        }

        return existing;
    }

    // ── Activity Log mappers ───────────────────────────────────────────────

    private ActivityLog toActivityLogModel(OfferActivityLogEntity entity) {
        OfferActivityLog log = new OfferActivityLog();
        log.activityName = entity.getEventId();
        log.activityComment = entity.getComment();
        log.activitySuccess = true;
        return log;
    }

    private OfferActivityLogEntity toActivityLogEntity(ActivityLog model) {
        OfferActivityLogEntity entity = new OfferActivityLogEntity();
        entity.setEventId(model.getName());
        entity.setComment(model.getComment());
        return entity;
    }
}

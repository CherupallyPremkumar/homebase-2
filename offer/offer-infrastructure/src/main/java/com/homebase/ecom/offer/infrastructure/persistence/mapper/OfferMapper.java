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
 */
public class OfferMapper {

    // ── DTO → Domain Model ─────────────────────────────────────────────────

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
        return model;
    }

    // ── Domain Model → DTO ─────────────────────────────────────────────────

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
        dto.setStatus(model.getCurrentState() != null ? model.getCurrentState().getStateId() : null);
        return dto;
    }

    // ── JPA Entity → Domain Model ──────────────────────────────────────────

    public Offer toModel(OfferEntity entity) {
        if (entity == null) return null;
        Offer model = new Offer();
        model.setId(entity.getId());
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
        model.setCurrentState(entity.getCurrentState());

        if (entity.getActivities() != null) {
            model.setActivities(entity.getActivities().stream()
                .map(this::toActivityLogModel)
                .collect(Collectors.toCollection(ArrayList::new)));
        }
        return model;
    }

    // ── Domain Model → JPA Entity ──────────────────────────────────────────

    public OfferEntity toEntity(Offer model) {
        if (model == null) return null;
        OfferEntity entity = new OfferEntity();
        entity.setId(model.getId());
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
        entity.setCurrentState(model.getCurrentState());

        if (model.getActivities() != null) {
            entity.setActivities(model.getActivities().stream()
                .map(this::toActivityLogEntity)
                .collect(Collectors.toList()));
        }
        return entity;
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

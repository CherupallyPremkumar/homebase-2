package com.homebase.ecom.offer.infrastructure.persistence.mapper;

import com.homebase.ecom.offer.api.dto.OfferDTO;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.model.OfferActivityLog;
import com.homebase.ecom.offer.infrastructure.persistence.entity.OfferEntity;
import com.homebase.ecom.offer.infrastructure.persistence.entity.OfferActivityLogEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class OfferMapper {

    public Offer toModel(OfferDTO dto) {
        if (dto == null) return null;
        Offer model = new Offer();
        model.setId(dto.getId());
        model.setVariantId(dto.getVariantId());
        model.setSupplierId(dto.getSupplierId());
        model.setPrice(dto.getPrice());
        model.setMsrp(dto.getMsrp());
        return model;
    }

    public Offer toModel(OfferEntity entity) {
        if (entity == null) return null;
        Offer model = new Offer();
        model.setId(entity.getId());
        model.setVariantId(entity.getVariantId());
        model.setSupplierId(entity.getSupplierId());
        model.setPrice(entity.getPrice());
        model.setMsrp(entity.getMsrp());
        model.setCurrentState(entity.getCurrentState());

        if (entity.getActivities() != null) {
            model.setActivities(entity.getActivities().stream()
                .map(this::toActivityLogModel)
                .collect(Collectors.toCollection(ArrayList::new)));
        }
        return model;
    }

    public OfferEntity toEntity(Offer model) {
        if (model == null) return null;
        OfferEntity entity = new OfferEntity();
        entity.setId(model.getId());
        entity.setVariantId(model.getVariantId());
        entity.setSupplierId(model.getSupplierId());
        entity.setPrice(model.getPrice());
        entity.setMsrp(model.getMsrp());
        entity.setCurrentState(model.getCurrentState());

        if (model.getActivities() != null) {
            entity.setActivities(model.getActivities().stream()
                .map(this::toActivityLogEntity)
                .collect(Collectors.toList()));
        }
        return entity;
    }

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

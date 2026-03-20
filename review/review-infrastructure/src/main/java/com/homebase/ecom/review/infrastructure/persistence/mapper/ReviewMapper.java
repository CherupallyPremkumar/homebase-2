package com.homebase.ecom.review.infrastructure.persistence.mapper;

import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.model.ReviewActivityLog;
import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewActivityLogEntity;
import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bidirectional mapper between Review domain model and ReviewEntity JPA entity.
 */
public class ReviewMapper {

    public Review toModel(ReviewEntity entity) {
        if (entity == null) return null;
        Review model = new Review();

        // Base entity fields
        model.setId(entity.getId());
        model.setVersion(entity.getVersion());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setCreatedBy(entity.getCreatedBy());
        model.setLastModifiedBy(entity.getLastModifiedBy());

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaLate(entity.getSlaLate());
        model.setSlaTendingLate(entity.getSlaTendingLate());

        // Business fields
        model.setProductId(entity.getProductId());
        model.setCustomerId(entity.getCustomerId());
        model.setOrderId(entity.getOrderId());
        model.setRating(entity.getRating());
        model.setTitle(entity.getTitle());
        model.setBody(entity.getBody());
        model.setVerifiedPurchase(entity.isVerifiedPurchase());
        model.setHelpfulCount(entity.getHelpfulCount());
        model.setReportCount(entity.getReportCount());
        model.setModeratorNotes(entity.getModeratorNotes());
        model.setVariantId(entity.getVariantId());
        model.setReviewSource(entity.getReviewSource());
        model.setTenant(entity.tenant);

        // Deserialize images JSON to List<String>
        if (entity.getImagesJson() != null && !entity.getImagesJson().isEmpty()) {
            model.setImages(deserializeImages(entity.getImagesJson()));
        }

        // Map activities
        if (entity.getActivities() != null) {
            for (ReviewActivityLogEntity actEntity : entity.getActivities()) {
                model.addActivity(actEntity.getName(), actEntity.getComment());
            }
        }

        return model;
    }

    public ReviewEntity toEntity(Review model) {
        if (model == null) return null;
        ReviewEntity entity = new ReviewEntity();

        // Base entity fields
        entity.setId(model.getId());
        if (model.getVersion() != null) {
            entity.setVersion(model.getVersion());
        }
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setLastModifiedBy(model.getLastModifiedBy());

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaLate(model.getSlaLate());
        entity.setSlaTendingLate(model.getSlaTendingLate());

        // Business fields
        entity.setProductId(model.getProductId());
        entity.setCustomerId(model.getCustomerId());
        entity.setOrderId(model.getOrderId());
        entity.setRating(model.getRating());
        entity.setTitle(model.getTitle());
        entity.setBody(model.getBody());
        entity.setVerifiedPurchase(model.isVerifiedPurchase());
        entity.setHelpfulCount(model.getHelpfulCount());
        entity.setReportCount(model.getReportCount());
        entity.setModeratorNotes(model.getModeratorNotes());
        entity.setVariantId(model.getVariantId());
        entity.setReviewSource(model.getReviewSource());
        entity.tenant = model.getTenant();

        // Serialize List<String> images to JSON
        if (model.getImages() != null && !model.getImages().isEmpty()) {
            entity.setImagesJson(serializeImages(model.getImages()));
        }

        // Map activities
        if (model.obtainActivities() != null) {
            ArrayList<ReviewActivityLogEntity> actEntities = new ArrayList<>();
            for (ActivityLog act : model.obtainActivities()) {
                ReviewActivityLogEntity actEntity = new ReviewActivityLogEntity();
                actEntity.activityName = act.getName();
                actEntity.activitySuccess = act.getSuccess();
                actEntity.activityComment = act.getComment();
                actEntities.add(actEntity);
            }
            entity.setActivities(actEntities);
        }

        return entity;
    }

    /**
     * Merges incoming domain model fields into an existing JPA entity.
     * Used by EntityStore for STM updates where the existing entity is loaded from DB
     * and incoming changes need to be applied without losing DB-managed fields.
     */
    public ReviewEntity mergeEntity(Review incoming, ReviewEntity existing) {
        if (incoming == null || existing == null) return existing;

        // Business fields — always overwrite from incoming
        existing.setProductId(incoming.getProductId());
        existing.setCustomerId(incoming.getCustomerId());
        existing.setOrderId(incoming.getOrderId());
        existing.setRating(incoming.getRating());
        existing.setTitle(incoming.getTitle());
        existing.setBody(incoming.getBody());
        existing.setVerifiedPurchase(incoming.isVerifiedPurchase());
        existing.setHelpfulCount(incoming.getHelpfulCount());
        existing.setReportCount(incoming.getReportCount());
        existing.setModeratorNotes(incoming.getModeratorNotes());
        existing.setVariantId(incoming.getVariantId());
        existing.setReviewSource(incoming.getReviewSource());

        // STM state fields
        existing.setCurrentState(incoming.getCurrentState());
        existing.setStateEntryTime(incoming.getStateEntryTime());
        existing.setSlaLate(incoming.getSlaLate());
        existing.setSlaTendingLate(incoming.getSlaTendingLate());

        // Tenant
        if (incoming.getTenant() != null) {
            existing.tenant = incoming.getTenant();
        }

        // Images
        if (incoming.getImages() != null && !incoming.getImages().isEmpty()) {
            existing.setImagesJson(serializeImages(incoming.getImages()));
        } else {
            existing.setImagesJson(null);
        }

        // Activities — replace with incoming
        if (incoming.obtainActivities() != null) {
            existing.getActivities().clear();
            for (ActivityLog act : incoming.obtainActivities()) {
                ReviewActivityLogEntity actEntity = new ReviewActivityLogEntity();
                actEntity.activityName = act.getName();
                actEntity.activitySuccess = act.getSuccess();
                actEntity.activityComment = act.getComment();
                existing.getActivities().add(actEntity);
            }
        }

        return existing;
    }

    /**
     * Simple pipe-delimited serialization for image URLs.
     * Avoids Jackson dependency in mapper.
     */
    private String serializeImages(List<String> images) {
        return String.join("|", images);
    }

    private List<String> deserializeImages(String imagesJson) {
        if (imagesJson == null || imagesJson.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(imagesJson.split("\\|")));
    }
}

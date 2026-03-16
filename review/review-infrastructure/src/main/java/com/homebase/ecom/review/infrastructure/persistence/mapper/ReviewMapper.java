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
        model.setId(entity.getId());
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
        model.setCurrentState(entity.getCurrentState());

        // Deserialize images JSON to List<String>
        if (entity.getImagesJson() != null && !entity.getImagesJson().isEmpty()) {
            model.setImages(deserializeImages(entity.getImagesJson()));
        }

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
        entity.setId(model.getId());
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
        entity.setCurrentState(model.getCurrentState());

        // Serialize List<String> images to JSON
        if (model.getImages() != null && !model.getImages().isEmpty()) {
            entity.setImagesJson(serializeImages(model.getImages()));
        }

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
     * Simple JSON-like serialization for image URLs.
     * Uses pipe-delimited format for simplicity (avoids Jackson dependency in mapper).
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

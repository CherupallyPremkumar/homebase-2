package com.homebase.ecom.review.infrastructure.persistence.mapper;

import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.model.ReviewImage;
import com.homebase.ecom.review.model.ReviewActivityLog;
import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewActivityLogEntity;
import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewEntity;
import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewImageEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReviewMapper {

    public Review toModel(ReviewEntity entity) {
        if (entity == null) return null;
        Review model = new Review();
        model.setId(entity.getId());
        model.setProductId(entity.getProductId());
        model.setUserId(entity.getUserId());
        model.setOrderId(entity.getOrderId());
        model.setRating(entity.getRating());
        model.setTitle(entity.getTitle());
        model.setBody(entity.getBody());
        model.setVerifiedPurchase(entity.isVerifiedPurchase());
        model.setHelpfulCount(entity.getHelpfulCount());
        model.setUnhelpfulCount(entity.getUnhelpfulCount());
        model.description = entity.getDescription();
        model.setCurrentState(entity.getCurrentState());

        if (entity.getImages() != null) {
            model.setImages(entity.getImages().stream()
                    .map(this::toImageModel)
                    .collect(Collectors.toList()));
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
        entity.setUserId(model.getUserId());
        entity.setOrderId(model.getOrderId());
        entity.setRating(model.getRating());
        entity.setTitle(model.getTitle());
        entity.setBody(model.getBody());
        entity.setVerifiedPurchase(model.isVerifiedPurchase());
        entity.setHelpfulCount(model.getHelpfulCount());
        entity.setUnhelpfulCount(model.getUnhelpfulCount());
        entity.setDescription(model.description);
        entity.setCurrentState(model.getCurrentState());

        if (model.getImages() != null) {
            entity.setImages(model.getImages().stream()
                    .map(this::toImageEntity)
                    .collect(Collectors.toList()));
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

    public ReviewImage toImageModel(ReviewImageEntity entity) {
        if (entity == null) return null;
        ReviewImage model = new ReviewImage();
        model.setId(entity.getId());
        model.setUrl(entity.getUrl());
        model.setAltText(entity.getAltText());
        model.setDisplayOrder(entity.getDisplayOrder());
        return model;
    }

    public ReviewImageEntity toImageEntity(ReviewImage model) {
        if (model == null) return null;
        ReviewImageEntity entity = new ReviewImageEntity();
        entity.setId(model.getId());
        entity.setUrl(model.getUrl());
        entity.setAltText(model.getAltText());
        entity.setDisplayOrder(model.getDisplayOrder());
        return entity;
    }
}

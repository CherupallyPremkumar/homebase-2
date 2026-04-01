package com.homebase.ecom.review.service.postSaveHooks;

import com.homebase.ecom.review.domain.port.ReviewEventPublisherPort;
import com.homebase.ecom.review.model.Review;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post save hook for the PUBLISHED state.
 * Publishes REVIEW_PUBLISHED event via domain port.
 * Product BC consumes this to update the product's average rating aggregate.
 */
public class PUBLISHEDReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(PUBLISHEDReviewPostSaveHook.class);

    @Autowired
    private ReviewEventPublisherPort reviewEventPublisherPort;

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        log.info("ReviewPublished: review {} for product {} rating {}",
                review.getId(), review.getProductId(), review.getRating());

        if (reviewEventPublisherPort != null) {
            reviewEventPublisherPort.publishReviewPublished(review);
        }
    }
}

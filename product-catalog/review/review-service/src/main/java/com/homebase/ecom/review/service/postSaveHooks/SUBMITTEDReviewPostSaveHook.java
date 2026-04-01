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
 * Post save hook for the SUBMITTED state.
 * Publishes REVIEW_SUBMITTED event via domain port.
 */
public class SUBMITTEDReviewPostSaveHook implements PostSaveHook<Review> {

    private static final Logger log = LoggerFactory.getLogger(SUBMITTEDReviewPostSaveHook.class);

    @Autowired
    private ReviewEventPublisherPort reviewEventPublisherPort;

    @Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        log.info("ReviewSubmitted: review {} for product {} by customer {}",
                review.getId(), review.getProductId(), review.getCustomerId());

        if (reviewEventPublisherPort != null) {
            reviewEventPublisherPort.publishReviewSubmitted(review);
        }
    }
}

package com.homebase.ecom.review.service.postSaveHooks;

import com.homebase.ecom.review.model.Review;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains customized post Save Hook for the APPROVED state.
 * Publishes ReviewApprovedEvent which triggers product rating recalculation.
 */
public class APPROVEDReviewPostSaveHook implements PostSaveHook<Review> {
    private static final Logger log = LoggerFactory.getLogger(APPROVEDReviewPostSaveHook.class);

	@Override
    public void execute(State startState, State endState, Review review, TransientMap map) {
        // Publish ReviewApprovedEvent - triggers product rating recalculation in Product BC
        log.info("ReviewApprovedEvent: Review {} for product {} approved. Rating: {}. " +
                "Triggering product rating recalculation.",
                review.getId(), review.getProductId(), review.getRating());

        // Increment product review count via event
        // The Product BC will consume this event and recalculate average rating
        map.put("eventType", "ReviewApprovedEvent");
        map.put("productId", review.getProductId());
        map.put("rating", review.getRating());
        map.put("verifiedPurchase", review.isVerifiedPurchase());
    }
}

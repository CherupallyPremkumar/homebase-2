package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.domain.port.PromoCommitPort;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OWIZ saga step 6: Commit coupon/promo usage.
 * Uses PromoCommitPort (hexagonal).
 */
public class CommitPromoCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(CommitPromoCommand.class);

    private final PromoCommitPort promoCommitPort;

    public CommitPromoCommand(PromoCommitPort promoCommitPort) {
        this.promoCommitPort = promoCommitPort;
    }

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        if (checkout.getCouponCodes() != null && !checkout.getCouponCodes().isEmpty()) {
            promoCommitPort.commitCoupons(
                    checkout.getId(),
                    checkout.getCustomerId(),
                    checkout.getCouponCodes()
            );
            log.info("[CHECKOUT SAGA] Committed {} coupons for checkout {}",
                    checkout.getCouponCodes().size(), checkout.getId());
        }

        checkout.setLastCompletedStep("commitPromo");
    }
}

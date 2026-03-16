package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.ReviewSupplierPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transition action for reviewSupplier event (APPLIED -> UNDER_REVIEW).
 * Admin picks up a supplier application for detailed review.
 */
public class ReviewSupplierAction extends AbstractSTMTransitionAction<Supplier, ReviewSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReviewSupplierAction.class);

    @Override
    public void transitionTo(Supplier supplier, ReviewSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) now under review",
                supplier.getBusinessName(), supplier.getId());
    }
}

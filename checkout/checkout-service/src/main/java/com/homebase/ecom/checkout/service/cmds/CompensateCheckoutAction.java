package com.homebase.ecom.checkout.service.cmds;

import com.homebase.ecom.cart.client.CartManagerClient;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.inventory.service.InventoryService;
import com.homebase.ecom.order.service.OrderService;
import com.homebase.ecom.payment.gateway.service.PaymentService;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action for 'compensate' event.
 * Rolls back saga steps in reverse order based on lastCompletedStep.
 * Uses module clients directly (typed delegates where available).
 */
public class CompensateCheckoutAction extends AbstractSTMTransitionAction<Checkout, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompensateCheckoutAction.class);

    @Autowired(required = false) private PaymentService paymentServiceClient;
    @Autowired(required = false) private OrderService orderServiceClient;
    @Autowired(required = false) private InventoryService inventoryServiceClient;
    @Autowired(required = false) private CartManagerClient cartManagerClient;

    @Override
    public void transitionTo(Checkout checkout, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String lastStep = checkout.getLastCompletedStep();
        log.info("[CHECKOUT] Compensating checkoutId={}, lastStep={}", checkout.getId(), lastStep);

        if (reachedStep(lastStep, "initiatePayment") && paymentServiceClient != null && checkout.getPaymentId() != null) {
            safeCompensate("cancelPayment", () -> {
                MinimalPayload cancelPayload = new MinimalPayload();
                cancelPayload.setComment("Compensating checkout " + checkout.getId());
                paymentServiceClient.processById(checkout.getPaymentId(), "cancel", cancelPayload);
            });
        }
        // commitPromo rollback — will be added when PromotionService exposes rollback API
        if (reachedStep(lastStep, "createOrder") && orderServiceClient != null) {
            safeCompensate("cancelOrder", () -> {
                orderServiceClient.proceed(checkout.getOrderId(), "cancel", null);
            });
        }
        if (reachedStep(lastStep, "reserveInventory") && inventoryServiceClient != null) {
            safeCompensate("releaseInventory", () -> {
                inventoryServiceClient.release(checkout.getId());
            });
        }
        // lockPrice — no explicit unlock needed (snapshot expires)
        if (reachedStep(lastStep, "lockCart") && cartManagerClient != null) {
            safeCompensate("unlockCart", () -> {
                cartManagerClient.cancelCheckout(checkout.getCartId());
            });
        }

        log.info("[CHECKOUT] Compensation complete for checkoutId={}", checkout.getId());
    }

    private static final String[] STEP_ORDER = {
        "lockCart", "lockPrice", "reserveInventory", "validateShipping",
        "createOrder", "commitPromo", "initiatePayment"
    };

    private boolean reachedStep(String lastCompletedStep, String targetStep) {
        if (lastCompletedStep == null) return false;
        int lastIdx = indexOf(lastCompletedStep);
        int targetIdx = indexOf(targetStep);
        return lastIdx >= targetIdx;
    }

    private int indexOf(String step) {
        for (int i = 0; i < STEP_ORDER.length; i++) {
            if (STEP_ORDER[i].equals(step)) return i;
        }
        return -1;
    }

    private void safeCompensate(String stepName, Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            log.error("[CHECKOUT] Compensation step '{}' failed: {}", stepName, e.getMessage(), e);
        }
    }
}

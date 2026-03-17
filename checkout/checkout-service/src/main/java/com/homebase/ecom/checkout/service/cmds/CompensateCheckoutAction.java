package com.homebase.ecom.checkout.service.cmds;

import com.homebase.ecom.checkout.domain.port.*;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for 'compensate' event.
 * Rolls back saga steps in reverse order based on lastCompletedStep.
 * Uses domain ports (hexagonal) — never concrete clients.
 */
public class CompensateCheckoutAction extends AbstractSTMTransitionAction<Checkout, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompensateCheckoutAction.class);

    private final CartLockPort cartLockPort;
    private final InventoryReservePort inventoryReservePort;
    private final OrderCreationPort orderCreationPort;
    private final PromoCommitPort promoCommitPort;
    private final PaymentInitiationPort paymentInitiationPort;

    public CompensateCheckoutAction(CartLockPort cartLockPort,
                                     InventoryReservePort inventoryReservePort,
                                     OrderCreationPort orderCreationPort,
                                     PromoCommitPort promoCommitPort,
                                     PaymentInitiationPort paymentInitiationPort) {
        this.cartLockPort = cartLockPort;
        this.inventoryReservePort = inventoryReservePort;
        this.orderCreationPort = orderCreationPort;
        this.promoCommitPort = promoCommitPort;
        this.paymentInitiationPort = paymentInitiationPort;
    }

    @Override
    public void transitionTo(Checkout checkout, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String lastStep = checkout.getLastCompletedStep();
        log.info("[CHECKOUT] Compensating checkoutId={}, lastStep={}", checkout.getId(), lastStep);

        if (reachedStep(lastStep, "initiatePayment") && checkout.getPaymentId() != null) {
            safeCompensate("cancelPayment", () ->
                    paymentInitiationPort.cancelPayment(checkout.getPaymentId()));
        }

        if (reachedStep(lastStep, "commitPromo") && checkout.getCouponCodes() != null && !checkout.getCouponCodes().isEmpty()) {
            safeCompensate("releaseCoupons", () ->
                    promoCommitPort.releaseCoupons(checkout.getId(), checkout.getCouponCodes()));
        }

        if (reachedStep(lastStep, "createOrder") && checkout.getOrderId() != null) {
            safeCompensate("cancelOrder", () ->
                    orderCreationPort.cancelOrder(checkout.getOrderId()));
        }

        if (reachedStep(lastStep, "reserveInventory")) {
            safeCompensate("releaseInventory", () ->
                    inventoryReservePort.release(checkout.getId()));
        }

        // lockPrice — no explicit unlock needed (price lock expires via TTL)

        if (reachedStep(lastStep, "lockCart")) {
            safeCompensate("unlockCart", () ->
                    cartLockPort.unlockCart(checkout.getCartId()));
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

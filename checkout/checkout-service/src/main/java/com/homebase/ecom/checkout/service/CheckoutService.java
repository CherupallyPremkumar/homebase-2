package com.homebase.ecom.checkout.service;

import com.homebase.ecom.checkout.domain.model.Checkout;
import com.homebase.ecom.checkout.domain.model.CheckoutState;
import com.homebase.ecom.checkout.domain.repository.CheckoutRepository;
import com.homebase.ecom.checkout.service.saga.CheckoutSagaOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class CheckoutService {
    private static final Logger log = LoggerFactory.getLogger(CheckoutService.class);

    private final CheckoutSagaOrchestrator sagaOrchestrator;
    private final CheckoutRepository checkoutRepository;

    public CheckoutService(CheckoutSagaOrchestrator sagaOrchestrator, CheckoutRepository checkoutRepository) {
        this.sagaOrchestrator = sagaOrchestrator;
        this.checkoutRepository = checkoutRepository;
    }

    @Transactional
    public Checkout initiateCheckout(UUID cartId, UUID userId, String couponCode, String idempotencyKey) {
        log.info("Initiating checkout for cart: {} and user: {}", cartId, userId);
        return sagaOrchestrator.executeCheckout(cartId, userId, couponCode, idempotencyKey);
    }

    public Checkout getCheckoutStatus(UUID checkoutId) {
        return checkoutRepository.findById(checkoutId)
            .orElseThrow(() -> new RuntimeException("Checkout not found: " + checkoutId));
    }

    @Transactional
    public void cancelCheckout(UUID checkoutId) {
        Checkout checkout = getCheckoutStatus(checkoutId);
        checkout.transitionTo(CheckoutState.CANCELLED);
        checkoutRepository.save(checkout);
        // Additional logic like releasing locks could be triggered here or via events
    }
}

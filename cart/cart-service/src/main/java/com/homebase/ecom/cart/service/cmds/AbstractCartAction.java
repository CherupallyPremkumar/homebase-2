package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.dto.OfferDto;
import com.homebase.ecom.inventory.dto.InventoryDto;
import com.homebase.ecom.cart.service.validator.CartPolicyValidator;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.pricing.api.dto.PricingRequestDTO;
import com.homebase.ecom.pricing.api.dto.PricingResponseDTO;
import com.homebase.ecom.pricing.client.PricingServiceClient;
import com.homebase.ecom.shared.dto.CartSnapshotDTO;
import com.homebase.ecom.shared.dto.CartSnapshotDTO.CartItemSnapshot;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.repository.InventoryRepository;
import com.homebase.ecom.cart.repository.ProductOfferRepository;

import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.homebase.ecom.cart.model.Cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for Cart STM actions to centralize common dependencies
 * and validations.
 */
public abstract class AbstractCartAction<P extends MinimalPayload> extends AbstractSTMTransitionAction<Cart, P> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ProductOfferRepository productOfferRepository;

    @Autowired
    protected InventoryRepository inventoryRepository;

    @Autowired
    protected CartPolicyValidator cartPolicyValidator;

    @Autowired
    @Lazy
    protected PricingServiceClient pricingServiceClient;

    /**
     * Common validation for product and stock.
     * 
     * @param productId The ID of the product.
     * @param quantity  The required quantity.
     * @return ProductOfferDetails if product is available and in stock.
     * @throws RuntimeException if validation fails.
     */
    protected OfferDto validateAndGetOffer(String productId, int quantity) {
        OfferDto offer = productOfferRepository.findById(productId);

        if (offer == null || !Boolean.TRUE.equals(offer.getActive())) {
            throw new RuntimeException("Product or Offer not available for productId: " + productId);
        }

        // Sync Stock Check using InventoryRepository
        InventoryDto inventory = inventoryRepository.findById(productId);
        if (inventory == null || (inventory.getQuantity() - inventory.getReserved()) < quantity) {
            throw new RuntimeException("Insufficient stock for productId: " + productId);
        }

        return offer;
    }

    /**
     * Delegates price calculation to the Pricing Service and updates the cart.
     */
    protected void recalculatePrices(Cart cart) {
        log.info("Recalculating prices for cart: {}", cart.getId());

        PricingRequestDTO request = new PricingRequestDTO();
        request.setCouponCode(cart.getAppliedPromoCode());

        CartSnapshotDTO snapshot = new CartSnapshotDTO();
        snapshot.setCartId(cart.getId());
        snapshot.setUserId(cart.getUserId());
        snapshot.setCurrency(cart.getTotalAmount() != null ? cart.getTotalAmount().getCurrency() : "INR");

        List<CartItemSnapshot> itemSnapshots = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            CartItemSnapshot itemSnapshot = new CartItemSnapshot();
            itemSnapshot.setProductId(item.getProductId());
            itemSnapshot.setQuantity(item.getQuantity());
            if (item.getPrice() != null) {
                itemSnapshot.setUnitPrice(item.getPrice().getAmount());
            }
            itemSnapshots.add(itemSnapshot);
        }
        snapshot.setItems(itemSnapshots);
        request.setCart(snapshot);

        try {
            PricingResponseDTO response = pricingServiceClient.calculatePrices(request);
            if (response != null && !response.isError()) {
                cart.setTotalAmount(response.getFinalTotal());
                // In a real scenario, we'd extract discount and tax from response too
                log.info("Price recalculation successful. New total: {}", response.getFinalTotal());
            } else {
                log.warn("Price recalculation failed: {}", response != null ? response.getMessage() : "Empty response");
            }
        } catch (Exception e) {
            log.error("Error calling Pricing Service", e);
            // Non-blocking for now, but in production this might need more robust handling
        }
    }

    /**
     * Guards: Security check for Customer/User driven events.
     * In a real implementation this would evaluate the security principal from a
     * ThreadLocal/Context.
     */
    protected void validateCustomerAccess() {
        // Implementation would check Security Context
        // if (!securityContext.isUser() || !securityContext.isOwner(cart.getUserId()))
        // throw SecurityException("...");
    }

    /**
     * Guards: Security check for System/Internal and Webhook driven events.
     */
    protected void validateSystemAccess() {
        // Implementation would check if the request originated internally or from a
        // trusted webhook source
        // if (!securityContext.isSystem() && !isTrustedSource()) throw
        // SecurityException("...");
    }

    /**
     * Common method to log important Cart activities.
     */
    protected void logActivity(Cart cart, String actionName, String description) {
        log.info("[CART ACTIVITY] CartId: {} | Action: {} | Msg: {}", cart.getId(), actionName, description);
        // Could also persist to a separate Audit/Activity log table.
    }


}

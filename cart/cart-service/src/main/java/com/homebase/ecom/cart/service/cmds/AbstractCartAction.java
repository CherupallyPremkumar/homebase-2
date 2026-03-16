package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.domain.model.PricingResult;
import com.homebase.ecom.cart.domain.port.InventoryCheckPort;
import com.homebase.ecom.cart.domain.port.PricingPort;
import com.homebase.ecom.cart.domain.port.ProductCheckPort;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.service.validator.CartPolicyValidator;
import com.homebase.ecom.shared.Money;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract base class for Cart STM actions.
 *
 * Responsibilities:
 * - Centralizes common dependencies (ports, validators)
 * - Orchestrates pricing: calls PricingPort (adapter returns PricingResult),
 *   then stores the result on the Cart
 * - Adapter is a translator only — this action is the orchestrator
 */
public abstract class AbstractCartAction<P extends MinimalPayload> extends AbstractSTMTransitionAction<Cart, P> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected CartPolicyValidator cartPolicyValidator;

    @Autowired
    protected InventoryCheckPort inventoryCheckPort;

    @Autowired
    protected PricingPort pricingPort;

    @Autowired
    protected ProductCheckPort productCheckPort;

    /**
     * Common method to log important Cart activities.
     */
    protected void logActivity(Cart cart, String actionName, String description) {
        log.info("[CART ACTIVITY] CartId: {} | Action: {} | Msg: {}", cart.getId(), actionName, description);
    }

    /**
     * Recalculates pricing and validates max cart value.
     * Use this in actions that can increase cart total (addItem, updateQuantity, merge).
     */
    protected void recalculatePricingAndValidateValue(Cart cart) {
        recalculatePricing(cart);
        cartPolicyValidator.validateCartValue(cart);
    }

    /**
     * Calls Pricing Service and stores the full result on the Cart.
     *
     * Flow:
     * 1. PricingPort (adapter) translates Cart→DTO, calls Pricing, returns PricingResult
     * 2. This method stores PricingResult on the Cart (subtotal, discount, total, line totals)
     *
     * Adapter = translator. Action = orchestrator that stores the result.
     */
    protected void recalculatePricing(Cart cart) {
        PricingResult result = pricingPort.calculatePricing(cart);
        storePricingResult(cart, result);
    }

    /**
     * Stores the pricing result on the Cart domain model.
     * Orchestrator's job — adapter just translates, action stores.
     */
    private void storePricingResult(Cart cart, PricingResult result) {
        String currency = cart.getCurrency();

        // Cart-level totals
        cart.setSubtotal(valueOrZero(result.getSubtotal(), currency));
        cart.setDiscountAmount(valueOrZero(result.getTotalDiscount(), currency));
        cart.setTotal(valueOrZero(result.getTotal(), currency));

        // Per-line-item pricing (Pricing may refresh unitPrice from catalog)
        if (result.getLineItems() != null) {
            for (PricingResult.LineItemPricing linePricing : result.getLineItems()) {
                cart.getItems().stream()
                        .filter(item -> item.getVariantId().equals(linePricing.getVariantId()))
                        .findFirst()
                        .ifPresent(item -> {
                            if (linePricing.getUnitPrice() != null) {
                                item.setUnitPrice(linePricing.getUnitPrice());
                            }
                            if (linePricing.getLineTotal() != null) {
                                item.setLineTotal(linePricing.getLineTotal());
                            }
                        });
            }
        }

        log.debug("Stored pricing for cartId={}: subtotal={}, discount={}, total={}",
                cart.getId(), cart.getSubtotal(), cart.getDiscountAmount(), cart.getTotal());
    }

    private Money valueOrZero(Money value, String currency) {
        return value != null ? value : Money.zero(currency);
    }

}

package com.homebase.ecom.cart.service.validator;

import com.homebase.ecom.cart.configuration.CartConfig;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.model.spec.*;
import com.homebase.ecom.cart.exception.CartLimitExceededException;
import com.homebase.ecom.cart.exception.CurrencyMismatchException;
import com.homebase.ecom.cart.exception.MultiSellerViolationException;
import com.homebase.ecom.cart.exception.QuantityLimitExceededException;
import com.homebase.ecom.dto.OfferDto;
import com.homebase.ecom.shared.CurrencyResolver;
import org.chenile.cconfig.sdk.CconfigClient;
import org.springframework.stereotype.Component;

/**
 * Enterprise policies enforcement layer for the Cart system.
 * Rules are delegated to Domain Specifications using context configurations.
 */
@Component
public class CartPolicyValidator {

    private final CconfigClient cconfigClient;
    private final CurrencyResolver currencyResolver;

    public CartPolicyValidator(CconfigClient cconfigClient, CurrencyResolver currencyResolver) {
        this.cconfigClient = cconfigClient;
        this.currencyResolver = currencyResolver;
    }

    private CartConfig getCartConfig() {
        try {
            java.util.Map<String, Object> map = cconfigClient.value("cart", null);
            if (map != null) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false);
                return mapper.convertValue(map, CartConfig.class);
            }
        } catch (Exception e) {
            // Fallback to default
        }
        return new CartConfig();
    }

    public void validate(Cart cart, CartItem newItem, OfferDto offer) {
        CartConfig config = getCartConfig();

        // 1. Multi-Seller Policy
        if (!new MultiSellerSpecification(config.getPolicies().getMulti_seller().isAllowed(), newItem.getSellerId())
                .isSatisfiedBy(cart)) {
            throw new MultiSellerViolationException(
                    "Multi-seller cart is not allowed. Items must be from the same seller.");
        }

        // 2. Max Items Policy
        if (!new MaxItemsSpecification(config.getPolicies().getLimits().getMaxItemsPerCart(), newItem.getProductId())
                .isSatisfiedBy(cart)) {
            throw new CartLimitExceededException("Cart limit exceeded. Maximum allowed unique items: " +
                    config.getPolicies().getLimits().getMaxItemsPerCart());
        }

        // 3. Max Quantity Policy
        if (!new MaxQuantitySpecification(config.getPolicies().getLimits().getMaxQuantityPerItem(),
                newItem.getQuantity())
                .isSatisfiedBy(cart)) {
            throw new QuantityLimitExceededException("Quantity limit exceeded for product " + newItem.getProductId() +
                    ". Maximum allowed: " + config.getPolicies().getLimits().getMaxQuantityPerItem());
        }

        // 4. Currency Policy
        String baseCurrency = currencyResolver.resolve().code();
        String offerCurrency = offer.getPrice().getCurrency();
        if (!new CurrencySpecification(config.getPolicies().getCurrency().isEnforceSingle(), baseCurrency,
                offerCurrency)
                .isSatisfiedBy(cart)) {
            throw new CurrencyMismatchException(
                    "Currency mismatch in cart. All items must share the same currency: " + baseCurrency);
        }
    }
}

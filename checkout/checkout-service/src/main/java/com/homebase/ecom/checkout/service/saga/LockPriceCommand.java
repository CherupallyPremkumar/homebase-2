package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.model.CheckoutItem;
import com.homebase.ecom.pricing.api.dto.CartItemSnapshotDTO;
import com.homebase.ecom.pricing.api.dto.CartSnapshotDTO;
import com.homebase.ecom.pricing.api.dto.PricingRequestDTO;
import com.homebase.ecom.pricing.api.dto.PricingResponseDTO;
import com.homebase.ecom.pricing.api.service.PricingService;
import org.chenile.owiz.Command;
import org.chenile.workflow.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * OWIZ saga step 2: Lock/snapshot prices for the checkout.
 * Calls pricing-client's PricingService to get a final price calculation
 * and stores the snapshot on the Checkout entity.
 */
public class LockPriceCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(LockPriceCommand.class);

    @Autowired(required = false)
    private PricingService pricingServiceClient;

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();
        if (pricingServiceClient != null) {
            PricingRequestDTO request = buildPricingRequest(checkout);
            PricingResponseDTO response = pricingServiceClient.lockPrice(request);

            if (response != null && !response.isError()) {
                if (response.getSubtotal() != null) checkout.setSubtotal(response.getSubtotal());
                if (response.getTotalDiscount() != null) checkout.setDiscountAmount(response.getTotalDiscount());
                if (response.getTaxAmount() != null) checkout.setTaxAmount(response.getTaxAmount());
                if (response.getShippingCost() != null) checkout.setShippingCost(response.getShippingCost());
                if (response.getFinalTotal() != null) checkout.setTotal(response.getFinalTotal());

                log.info("[CHECKOUT SAGA] Prices locked for checkout {}, total={}",
                        checkout.getId(), checkout.getTotal());
            } else {
                String msg = response != null ? response.getMessage() : "Pricing service returned null";
                throw new RuntimeException("Price lock failed: " + msg);
            }
        }
        checkout.setLastCompletedStep("lockPrice");
    }

    private PricingRequestDTO buildPricingRequest(Checkout checkout) {
        CartSnapshotDTO cartSnapshot = new CartSnapshotDTO();
        cartSnapshot.setCartId(checkout.getCartId());
        cartSnapshot.setUserId(checkout.getCustomerId());

        List<CartItemSnapshotDTO> items = new ArrayList<>();
        for (CheckoutItem ci : checkout.getItems()) {
            CartItemSnapshotDTO item = new CartItemSnapshotDTO();
            item.setProductId(ci.getProductId());
            item.setVariantId(ci.getVariantId());
            item.setQuantity(ci.getQuantity());
            item.setBasePrice(ci.getUnitPrice());
            item.setSellerId(ci.getSupplierId());
            items.add(item);
        }
        cartSnapshot.setItems(items);

        PricingRequestDTO request = new PricingRequestDTO();
        request.setCart(cartSnapshot);
        if (checkout.getCouponCodes() != null && !checkout.getCouponCodes().isEmpty()) {
            request.setCouponCodes(checkout.getCouponCodes());
            request.setCouponCode(checkout.getCouponCodes().get(0));
        }
        return request;
    }
}

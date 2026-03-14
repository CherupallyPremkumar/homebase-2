package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.dto.ApplyPromoCodePayload;
import com.homebase.ecom.shared.Money;
import com.homebase.ecom.promo.dto.PromoApplicationResult;
import com.homebase.ecom.promo.dto.PromoCartDTO;
import com.homebase.ecom.promo.dto.PromoCartItemDTO;
import com.homebase.ecom.promo.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Action to handle applying a promo code to the cart.
 */
public class ApplyPromoCodeAction extends AbstractCartAction<ApplyPromoCodePayload> {

    @Autowired
    private PromotionService promotionService;

    @Override
    public void transitionTo(Cart cart,
            ApplyPromoCodePayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String promoCode = payload.getPromoCode();
        PromoCartDTO promoCartDTO = mapToPromoDTO(cart);

        PromoApplicationResult result = promotionService.applyPromo(promoCartDTO, promoCode);

        String currency = (cart.getTotalAmount() != null) ? cart.getTotalAmount().getCurrency() : "INR";

        if (result.isValid()) {
            cart.setAppliedPromoCode(promoCode);
            double discountAmt = result.getDiscountAmount() != null ? result.getDiscountAmount() : 0.0;
            cart.setDiscountAmount(new Money(BigDecimal.valueOf(discountAmt), currency));

            // Recalculate total if needed (assuming totalAmount should represent final
            // price)
            // Note: In some designs, totalAmount is gross and discount is stored
            // separately.
            // We'll follow the existing pattern of updating the total.
            double initialTotal = calculateGrossTotal(cart);
            cart.setTotalAmount(new Money(BigDecimal.valueOf(initialTotal - discountAmt), currency));
        } else {
            cart.getTransientMap().put("promoError", result.getReasonIfInvalid());
            // Optionally clear existing promo if the new one is invalid
            cart.setAppliedPromoCode(null);
            cart.setDiscountAmount(new Money(BigDecimal.ZERO, currency));
        }
    }

    private PromoCartDTO mapToPromoDTO(Cart cart) {
        PromoCartDTO dto = new PromoCartDTO();
        dto.setCartId(cart.getId());
        dto.setTotalAmount(calculateGrossTotal(cart));

        List<PromoCartItemDTO> itemDTOs = new ArrayList<>();
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                PromoCartItemDTO itemDTO = new PromoCartItemDTO();
                itemDTO.setProductId(item.getProductId());
                itemDTO.setQuantity(item.getQuantity());
                if (item.getPrice() != null && item.getPrice().getAmount() != null) {
                    itemDTO.setPrice(item.getPrice().getAmount().doubleValue());
                }
                itemDTOs.add(itemDTO);
            }
        }
        dto.setItems(itemDTOs);
        return dto;
    }

    private double calculateGrossTotal(Cart cart) {
        double total = 0;
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getPrice() != null && item.getPrice().getAmount() != null && item.getQuantity() != null) {
                    total += item.getPrice().getAmount().doubleValue() * item.getQuantity();
                }
            }
        }
        return total;
    }
}

package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.MergeCartPayload;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import org.chenile.stm.STM;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * STM transition action for merge event.
 * Fetches the source (guest) cart by sourceCartId, merges its items
 * into the target (user) cart. Duplicate variantIds aggregate quantities.
 * After merge, transitions the source cart to MERGED state and stores
 * targetCartId in the source cart's transient map for the PostSaveHook.
 */
public class MergeCartAction extends AbstractCartAction<MergeCartPayload> {

    @Autowired
    @Qualifier("cartEntityStore")
    private EntityStore<Cart> entityStore;

    @Autowired
    @Qualifier("cartEntityStm")
    private STM<Cart> cartStm;

    @Override
    public void transitionTo(Cart cart, MergeCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.sourceCartId == null || payload.sourceCartId.isBlank()) {
            throw new IllegalArgumentException("sourceCartId is required for merge");
        }

        Cart sourceCart = entityStore.retrieve(payload.sourceCartId);
        if (sourceCart == null) {
            throw new IllegalArgumentException("Source cart not found: " + payload.sourceCartId);
        }

        if (sourceCart.getItems() == null || sourceCart.getItems().isEmpty()) {
            logActivity(cart, "merge", "Source cart " + payload.sourceCartId + " has no items, nothing to merge");
            return;
        }

        int mergedCount = 0;
        for (CartItem sourceItem : sourceCart.getItems()) {
            int existingQty = cart.getItems().stream()
                    .filter(i -> i.getVariantId().equals(sourceItem.getVariantId()))
                    .mapToInt(CartItem::getQuantity)
                    .findFirst().orElse(0);
            int totalQty = existingQty + sourceItem.getQuantity();

            cartPolicyValidator.validateQuantity(totalQty);

            boolean isNewItem = existingQty == 0;
            if (isNewItem) {
                cartPolicyValidator.validateItemCount(cart);
            }

            cart.addItem(sourceItem);
            mergedCount++;
        }

        recalculatePricingAndValidateValue(cart);

        // Store targetCartId in transient map for the MERGED PostSaveHook on source cart
        sourceCart.getTransientMap().put("targetCartId", cart.getId());

        // Transition source cart to MERGED state via STM (markMerged is a SYSTEM event)
        MinimalPayload mergedPayload = new MinimalPayload();
        cartStm.proceed(sourceCart, "markMerged", mergedPayload);
        entityStore.store(sourceCart);

        logActivity(cart, "merge",
                "Merged " + mergedCount + " items from cart " + payload.sourceCartId + ", total: " + cart.getTotal());
    }
}

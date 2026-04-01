package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.dto.RemoveAddressPayload;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * ACTIVE -> ACTIVE (self-transition).
 *
 * Removes an address from the user's address book.
 * If the removed address was the default, the first remaining address is promoted.
 */
public class RemoveAddressAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        if (payload instanceof RemoveAddressPayload removeAddressPayload) {
            if (removeAddressPayload.getAddressId() == null || removeAddressPayload.getAddressId().isBlank()) {
                throw new IllegalArgumentException("Address ID is required for removal");
            }

            String addressId = removeAddressPayload.getAddressId();

            // Domain aggregate handles default-promotion logic
            user.removeAddress(addressId);

            user.getTransientMap().put("event", "ADDRESS_REMOVED");
            user.getTransientMap().put("addressId", addressId);
        } else {
            throw new IllegalArgumentException("RemoveAddressPayload is required");
        }
    }
}

package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.Address;
import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * ACTIVE -> ACTIVE (self-transition).
 *
 * Adds a new address to the user's address book.
 * Business invariants enforced by the User aggregate:
 *   - Max 5 addresses per user
 *   - First address auto-becomes default
 *
 * Validates required address fields before delegating to domain.
 */
public class AddAddressAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        if (payload instanceof Address address) {
            // Validate required address fields
            if (address.getLine1() == null || address.getLine1().isBlank()) {
                throw new IllegalArgumentException("Address line1 is required");
            }
            if (address.getCity() == null || address.getCity().isBlank()) {
                throw new IllegalArgumentException("City is required");
            }
            if (address.getZip() == null || address.getZip().isBlank()) {
                throw new IllegalArgumentException("ZIP/Pin code is required");
            }

            // Domain aggregate enforces max-5 invariant
            user.addAddress(address);

            user.getTransientMap().put("event", "ADDRESS_ADDED");
            user.getTransientMap().put("addressId", address.getId());
            user.getTransientMap().put("isDefault", String.valueOf(address.isDefault()));
        } else {
            throw new IllegalArgumentException("Address payload is required");
        }
    }
}

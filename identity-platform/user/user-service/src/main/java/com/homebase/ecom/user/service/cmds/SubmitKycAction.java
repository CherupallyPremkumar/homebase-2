package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.dto.SubmitKycUserPayload;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * ACTIVE -> KYC_PENDING.
 *
 * Customer/Seller submits KYC documents for verification.
 * Sets kycStatus to KYC_PENDING.
 */
public class SubmitKycAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        user.setKycStatus("KYC_PENDING");

        user.getTransientMap().put("event", "KYC_SUBMITTED");
    }
}

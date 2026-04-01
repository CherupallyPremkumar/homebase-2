package com.homebase.ecom.user.service.cmds;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.dto.VerifyKycUserPayload;
import org.chenile.stm.action.STMTransitionAction;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

/**
 * KYC_PENDING -> KYC_VERIFIED.
 *
 * Admin verifies user's KYC documents.
 * Sets kycStatus to KYC_VERIFIED.
 */
public class VerifyKycAction implements STMTransitionAction<User> {
    @Override
    public void doTransition(User user, Object payload, State fromState, String eventId,
                             State toState, STMInternalTransitionInvoker<?> stm,
                             org.chenile.stm.model.Transition transition) throws Exception {

        user.setKycStatus("KYC_VERIFIED");

        user.getTransientMap().put("event", "KYC_VERIFIED");
    }
}

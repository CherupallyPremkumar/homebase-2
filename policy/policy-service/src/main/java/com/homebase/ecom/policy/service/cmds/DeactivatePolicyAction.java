package com.homebase.ecom.policy.service.cmds;

import com.homebase.ecom.policy.api.dto.PolicyEventPayload;
import com.homebase.ecom.policy.domain.model.Policy;
import org.springframework.stereotype.Component;

public class DeactivatePolicyAction extends AbstractPolicyAction<PolicyEventPayload> {
    @Override
    protected void doExecute(Policy policy, PolicyEventPayload payload) throws Exception {
        policy.setActive(false);
        System.out.println("Deactivating policy: " + policy.getId());
    }
}

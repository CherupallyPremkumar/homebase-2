package com.homebase.ecom.policy.service.cmds;

import com.homebase.ecom.policy.api.dto.PolicyEventPayload;
import com.homebase.ecom.policy.domain.model.Policy;

public class ApprovePolicyAction extends AbstractPolicyAction<PolicyEventPayload> {
    @Override
    protected void doExecute(Policy policy, PolicyEventPayload payload) throws Exception {
        System.out.println("Approving policy: " + policy.getId());
    }
}

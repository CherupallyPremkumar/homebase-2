package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import org.chenile.cconfig.sdk.CconfigClient;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * Validates the supplier's submitted documents and basic information.
 * Incorporates the old ValidateOnboardingRequest OWIZ command logic.
 */
public class ValidateDocumentsAction extends AbstractSTMTransitionAction<OnboardingSaga, MinimalPayload> {

    @Autowired
    private CconfigClient cconfigClient;

    @Override
    public void transitionTo(OnboardingSaga saga, MinimalPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        if (saga.getSupplierName() == null || saga.getSupplierName().trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier name is mandatory");
        }

        if (saga.getEmail() == null || !saga.getEmail().contains("@")) {
            throw new IllegalArgumentException("Valid email is mandatory");
        }

        // Dynamic rule validation from cconfig
        Map<String, Object> map = cconfigClient.value("on-boarding", null);
        if (map != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.valueToTree(map);
            JsonNode requiredNode = node.at("/rules/validation/requiredAttributes");

            if (!requiredNode.isMissingNode() && requiredNode.isArray()) {
                for (JsonNode item : requiredNode) {
                    String attr = item.asText();
                    // Validate known fields
                    if ("upiId".equals(attr) && (saga.getUpiId() == null || saga.getUpiId().trim().isEmpty())) {
                        throw new IllegalArgumentException("Missing required attribute: " + attr);
                    }
                    if ("phone".equals(attr) && (saga.getPhone() == null || saga.getPhone().trim().isEmpty())) {
                        throw new IllegalArgumentException("Missing required attribute: " + attr);
                    }
                }
            }
        }

        saga.getTransientMap().put("documentsValidated", true);
    }
}

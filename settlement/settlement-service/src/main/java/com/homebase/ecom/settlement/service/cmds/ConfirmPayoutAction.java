package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.ConfirmPayoutPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.chenile.cconfig.sdk.CconfigClient;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;

/**
 * Action to handle manual payout confirmation by Daddy.
 * Records the reference and transitions the settlement to SETTLED.
 */
public class ConfirmPayoutAction extends AbstractSTMTransitionAction<Settlement, ConfirmPayoutPayload> {

        @Autowired
        private CconfigClient cconfigClient;

        private static final Logger log = LoggerFactory.getLogger(ConfirmPayoutAction.class);

        @Override
        public void transitionTo(Settlement settlement,
                        ConfirmPayoutPayload payload,
                        State startState, String eventId,
                        State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

                // --- Payout Threshold Enforcement ---
                try {
                        java.util.Map<String, Object> map = cconfigClient.value("settlement", null);
                        if (map != null) {
                                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                                com.fasterxml.jackson.databind.JsonNode node = mapper.valueToTree(map);
                                com.fasterxml.jackson.databind.JsonNode minNode = node.at("/rules/payout/minBalance");

                                if (!minNode.isMissingNode() && minNode.isNumber()) {
                                        BigDecimal threshold = BigDecimal.valueOf(minNode.asDouble());
                                        BigDecimal netPayout = settlement.getNetPayoutAmount().getAmount();
                                        if (netPayout.compareTo(threshold) < 0) {
                                                throw new RuntimeException("Payout amount " + netPayout +
                                                                " is below minimum payout threshold of " + threshold);
                                        }
                                }
                        }
                } catch (RuntimeException re) {
                        throw re;
                } catch (Exception e) {
                        throw new RuntimeException("Could not verify payout threshold policy", e);
                }

                settlement.getTransientMap().previousPayload = payload;

                String ref = (payload != null && payload.getPaymentReference() != null)
                                ? payload.getPaymentReference()
                                : "Manual Payout";

                log.info("Confirming manual payout for supplier: {}, Amount: {}, Reference: {}",
                                settlement.getSupplierId(), settlement.getNetPayoutAmount(), ref);

        }
}

package com.homebase.ecom.onboarding.service.cmds;

import com.homebase.ecom.onboarding.model.OnboardingSaga;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.service.SupplierService;
import org.chenile.cconfig.sdk.CconfigClient;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * Creates the supplier record in the supplier-service after onboarding approval.
 * Incorporates the old CreateSupplierRecordCommand logic.
 */
public class CreateSupplierAction extends AbstractSTMTransitionAction<OnboardingSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CreateSupplierAction.class);

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CconfigClient cconfigClient;

    @Override
    public void transitionTo(OnboardingSaga saga, MinimalPayload payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) throws Exception {

        Supplier supplier = new Supplier();
        supplier.setName(saga.getSupplierName());
        supplier.setEmail(saga.getEmail());
        supplier.setDescription(saga.getDescription());
        supplier.setPhone(saga.getPhone());
        supplier.setUpiId(saga.getUpiId());
        supplier.setAddress(saga.getAddress());

        Double commission = saga.getCommissionPercentage();
        if (commission == null) {
            Map<String, Object> map = cconfigClient.value("on-boarding", null);
            if (map != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.valueToTree(map);
                JsonNode commNode = node.at("/rules/finances/commissionDefault");
                if (!commNode.isMissingNode() && commNode.isNumber()) {
                    commission = commNode.asDouble();
                }
            }
            if (commission == null) {
                commission = 10.0; // Fallback
            }
        }
        supplier.setCommissionPercentage(commission);

        StateEntityServiceResponse<Supplier> response = supplierService.create(supplier);

        if (response != null && response.getMutatedEntity() != null) {
            saga.setSupplierId(response.getMutatedEntity().getId());
            log.info("Supplier record created: id={}, name={}", saga.getSupplierId(), saga.getSupplierName());
        } else {
            throw new RuntimeException("Failed to create supplier in supplier-service");
        }
    }
}

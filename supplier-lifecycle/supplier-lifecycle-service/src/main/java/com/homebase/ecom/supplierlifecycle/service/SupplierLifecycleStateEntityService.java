package com.homebase.ecom.supplierlifecycle.service;

import org.chenile.stm.STM;
import org.chenile.stm.State;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;

import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;

/**
 * Custom StateEntityService for supplier lifecycle sagas.
 * Supports multi-flow STM: if the entity's flowId is set (e.g., "reactivate-flow"),
 * the create method uses that flow's initial state instead of the default flow.
 *
 * The base StateEntityServiceImpl.create() always nullifies the state before proceeding,
 * which forces the default flow. This override sets a State with flowId (and null stateId)
 * so the STM's correctState() resolves to the correct flow's initial state.
 */
public class SupplierLifecycleStateEntityService extends HmStateEntityServiceImpl<SupplierLifecycleSaga> {

    public SupplierLifecycleStateEntityService(STM<SupplierLifecycleSaga> stm,
                                                STMActionsInfoProvider stmActionsInfoProvider,
                                                EntityStore<SupplierLifecycleSaga> entityStore) {
        super(stm, stmActionsInfoProvider, entityStore);
    }

    @Override
    public StateEntityServiceResponse<SupplierLifecycleSaga> create(SupplierLifecycleSaga entity) {
        String flowId = entity.getFlowId();
        if (flowId != null && !flowId.isEmpty()) {
            // Set state with flowId and null stateId so STM resolves the flow's initial state.
            entity.setCurrentState(new State(null, flowId));
        } else {
            // Default behavior: null state triggers default flow
            entity.setCurrentState(null);
        }
        SupplierLifecycleSaga ret = processEntity(entity, null, null);
        return makeStateEntityResponse(ret);
    }
}

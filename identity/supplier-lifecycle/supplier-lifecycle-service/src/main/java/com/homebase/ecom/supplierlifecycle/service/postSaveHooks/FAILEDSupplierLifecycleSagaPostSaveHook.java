package com.homebase.ecom.supplierlifecycle.service.postSaveHooks;

import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

public class FAILEDSupplierLifecycleSagaPostSaveHook implements PostSaveHook<SupplierLifecycleSaga> {
    @Override
    public void execute(State startState, State endState, SupplierLifecycleSaga saga, TransientMap map) {
    }
}

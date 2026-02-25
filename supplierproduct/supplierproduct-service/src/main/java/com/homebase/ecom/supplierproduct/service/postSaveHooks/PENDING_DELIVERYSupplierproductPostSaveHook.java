package com.homebase.ecom.supplierproduct.service.postSaveHooks;

import com.homebase.ecom.supplierproduct.model.Supplierproduct;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class PENDING_DELIVERYSupplierproductPostSaveHook implements PostSaveHook<Supplierproduct>{
	@Override
    public void execute(State startState, State endState, Supplierproduct supplierproduct, TransientMap map){
    }
}

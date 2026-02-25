package com.homebase.ecom.supplier.service.postSaveHooks;

import com.homebase.ecom.supplier.model.Supplier;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class SUSPENDEDSupplierPostSaveHook implements PostSaveHook<Supplier>{
	@Override
    public void execute(State startState, State endState, Supplier supplier, TransientMap map){
    }
}

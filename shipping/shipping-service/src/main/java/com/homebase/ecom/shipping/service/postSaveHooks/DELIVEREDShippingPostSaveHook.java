package com.homebase.ecom.shipping.service.postSaveHooks;

import com.homebase.ecom.shipping.model.Shipping;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class DELIVEREDShippingPostSaveHook implements PostSaveHook<Shipping>{
	@Override
    public void execute(State startState, State endState, Shipping shipping, TransientMap map){
    }
}

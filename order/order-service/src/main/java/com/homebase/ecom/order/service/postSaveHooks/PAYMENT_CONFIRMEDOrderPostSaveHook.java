package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class PAYMENT_CONFIRMEDOrderPostSaveHook implements PostSaveHook<Order>{
	@Override
    public void execute(State startState, State endState, Order order, TransientMap map){
    }
}

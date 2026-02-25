package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class CHECKOUT_INITIATEDCartPostSaveHook implements PostSaveHook<Cart>{
	@Override
    public void execute(State startState, State endState, Cart cart, TransientMap map){
    }
}

package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.model.Inventory;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class DISCARDEDInventoryPostSaveHook implements PostSaveHook<Inventory>{
	@Override
    public void execute(State startState, State endState, Inventory inventory, TransientMap map){
    }
}

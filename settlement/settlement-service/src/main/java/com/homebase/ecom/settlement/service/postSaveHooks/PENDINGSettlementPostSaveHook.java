package com.homebase.ecom.settlement.service.postSaveHooks;

import com.homebase.ecom.settlement.model.Settlement;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class PENDINGSettlementPostSaveHook implements PostSaveHook<Settlement>{
	@Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map){
    }
}

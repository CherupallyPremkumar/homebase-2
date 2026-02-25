package com.homebase.ecom.returnrequest.service.postSaveHooks;

import com.homebase.ecom.returnrequest.model.Returnrequest;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class REJECTEDReturnrequestPostSaveHook implements PostSaveHook<Returnrequest>{
	@Override
    public void execute(State startState, State endState, Returnrequest returnrequest, TransientMap map){
    }
}

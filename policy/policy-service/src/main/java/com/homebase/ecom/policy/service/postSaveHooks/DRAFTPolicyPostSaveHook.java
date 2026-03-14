package com.homebase.ecom.policy.service.postSaveHooks;

import com.homebase.ecom.policy.domain.model.Policy;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class DRAFTPolicyPostSaveHook implements PostSaveHook<Policy>{
	@Override
    public void execute(State startState, State endState, Policy policy, TransientMap map){
    }
}

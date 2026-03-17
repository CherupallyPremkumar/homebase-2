package com.homebase.ecom.rulesengine.service.postSaveHooks;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class ACTIVERuleSetPostSaveHook implements PostSaveHook<RuleSet>{
	@Override
    public void execute(State startState, State endState, RuleSet ruleSet, TransientMap map){
    }
}

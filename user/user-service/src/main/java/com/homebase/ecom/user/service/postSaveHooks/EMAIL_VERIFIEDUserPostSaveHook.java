package com.homebase.ecom.user.service.postSaveHooks;

import com.homebase.ecom.user.model.User;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the State ID.
*/
public class EMAIL_VERIFIEDUserPostSaveHook implements PostSaveHook<User>{
	@Override
    public void execute(State startState, State endState, User user, TransientMap map){
    }
}

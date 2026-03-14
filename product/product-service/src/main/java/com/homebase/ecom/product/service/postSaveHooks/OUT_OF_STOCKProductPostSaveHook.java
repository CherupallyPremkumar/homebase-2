package com.homebase.ecom.product.service.postSaveHooks;

import com.homebase.ecom.product.domain.model.Product;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 Contains customized post Save Hook for the OUT_OF_STOCK State ID.
*/
public class OUT_OF_STOCKProductPostSaveHook implements PostSaveHook<Product>{
	@Override
    public void execute(State startState, State endState, Product product, TransientMap map){
    }
}

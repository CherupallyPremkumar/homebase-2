package com.homebase.ecom.user.service.impl;

import com.homebase.ecom.user.domain.model.User;
import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;

/**
 * UserServiceImpl — orchestration layer for the User STM lifecycle.
 *
 * Extends StateEntityServiceImpl (Chenile) which handles all CRUD + STM transitions.
 * NO business logic here — that lives in STM action classes.
 * NO @Autowired — wired via constructor in UserConfiguration.
 */
public class UserServiceImpl extends StateEntityServiceImpl<User> {

    public UserServiceImpl(STM<User> stm,
                           STMActionsInfoProvider actionsInfoProvider,
                           EntityStore<User> entityStore) {
        super(stm, actionsInfoProvider, entityStore);
    }
}

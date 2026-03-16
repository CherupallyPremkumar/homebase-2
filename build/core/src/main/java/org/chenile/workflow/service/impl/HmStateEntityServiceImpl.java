package org.chenile.workflow.service.impl;

import org.chenile.base.exception.ErrorNumException;
import org.chenile.stm.STM;
import org.chenile.stm.StateEntity;
import org.chenile.stm.exception.STMException;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;

/**
 * Extends StateEntityServiceImpl to also map STMException.UNAVAILABLE_TRANSITION (550)
 * to HTTP 422 instead of 500. The base class only maps INVALID_EVENTID and INVALID_TRANSITION.
 */
public class HmStateEntityServiceImpl<T extends StateEntity> extends StateEntityServiceImpl<T> {

    public HmStateEntityServiceImpl(STM<T> stm, STMActionsInfoProvider stmActionsInfoProvider,
                                    EntityStore<T> entityStore) {
        super(stm, stmActionsInfoProvider, entityStore);
    }

    @Override
    protected T processEntity(T entity, String event, Object payload) {
        try {
            return super.processEntity(entity, event, payload);
        } catch (ErrorNumException e) {
            if (e.getErrorNum() == 500 && e.getCause() instanceof STMException stmEx
                    && stmEx.getMessageId() == STMException.UNAVAILABLE_TRANSITION) {
                throw new ErrorNumException(422, 6001,
                        "Invalid event or transition: Error = " + stmEx.getMessage());
            }
            throw e;
        }
    }
}

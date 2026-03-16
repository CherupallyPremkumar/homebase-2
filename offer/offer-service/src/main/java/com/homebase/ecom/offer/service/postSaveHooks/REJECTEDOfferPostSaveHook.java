package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.NotificationPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post save hook for REJECTED state.
 * Notifies seller their offer was rejected.
 */
public class REJECTEDOfferPostSaveHook implements PostSaveHook<Offer> {
    private static final Logger log = LoggerFactory.getLogger(REJECTEDOfferPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        log.info("Offer {} by seller {} was rejected", offer.getId(), offer.getSupplierId());
        if (notificationPort != null) {
            notificationPort.notifyOfferRejected(offer.getSupplierId(), offer.getId(), "See activity log for details");
        }
    }
}

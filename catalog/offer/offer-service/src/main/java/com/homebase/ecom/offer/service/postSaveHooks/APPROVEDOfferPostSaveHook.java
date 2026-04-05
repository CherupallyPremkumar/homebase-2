package com.homebase.ecom.offer.service.postSaveHooks;

import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.NotificationPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for APPROVED state.
 * Notifies seller their offer was approved.
 */
public class APPROVEDOfferPostSaveHook implements PostSaveHook<Offer> {
    private static final Logger log = LoggerFactory.getLogger(APPROVEDOfferPostSaveHook.class);

    private final NotificationPort notificationPort;

    public APPROVEDOfferPostSaveHook(NotificationPort notificationPort) {
        this.notificationPort = notificationPort;
    }

    @Override
    public void execute(State startState, State endState, Offer offer, TransientMap map) {
        log.info("Offer {} approved for product {} by seller {}",
                offer.getId(), offer.getProductId(), offer.getSupplierId());
        if (notificationPort != null) {
            notificationPort.notifyOfferApproved(offer.getSupplierId(), offer.getId());
        }
    }
}

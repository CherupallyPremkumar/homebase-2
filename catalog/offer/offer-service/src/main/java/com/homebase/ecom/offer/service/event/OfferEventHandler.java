package com.homebase.ecom.offer.service.event;

import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.port.OfferRepository;
import com.homebase.ecom.shared.event.*;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Item 10: Chenile event handler for offer cross-service events.
 * Registered via offerEventService.json — operations subscribe to Kafka topics.
 *
 * CONSUMES:
 * - product.events: PRODUCT_DISCONTINUED -> expire related offers
 * - inventory.events: STOCK_DEPLETED (OUT_OF_STOCK) -> suspend related offers
 *
 * Bean name "offerEventService" must match the service JSON id.
 */
public class OfferEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OfferEventHandler.class);

    private final OfferRepository offerRepository;
    private final StateEntityServiceImpl<Offer> offerStateEntityService;
    private final ObjectMapper objectMapper;

    public OfferEventHandler(
            OfferRepository offerRepository,
            @Qualifier("_offerStateEntityService_") StateEntityServiceImpl<Offer> offerStateEntityService,
            ObjectMapper objectMapper) {
        this.offerRepository = offerRepository;
        this.offerStateEntityService = offerStateEntityService;
        this.objectMapper = objectMapper;
    }

    // ── product.events ─────────────────────────────────────────────────────

    /**
     * When a product is discontinued, expire all live/suspended offers for that product.
     */
    @Transactional
    public void handleProductEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        if (ProductDiscontinuedEvent.EVENT_TYPE.equals(envelope.getEventType())) {
            ProductDiscontinuedEvent event = objectMapper.convertValue(
                    envelope.getPayload(), ProductDiscontinuedEvent.class);
            log.info("Offer: received PRODUCT_DISCONTINUED for product: {}", event.getProductId());

            try {
                List<Offer> liveOffers = offerRepository.findLiveOffersByProductId(event.getProductId());
                for (Offer offer : liveOffers) {
                    try {
                        MinimalPayload payload = new MinimalPayload();
                        payload.setComment("Product discontinued: " + event.getReason());
                        offerStateEntityService.processById(offer.getId(), "expire", payload);
                        log.info("Expired offer {} due to product discontinuation", offer.getId());
                    } catch (Exception e) {
                        log.warn("Could not expire offer {} for discontinued product {}: {}",
                                offer.getId(), event.getProductId(), e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.error("Error processing PRODUCT_DISCONTINUED for product: {}", event.getProductId(), e);
            }
        }
    }

    // ── inventory.events ───────────────────────────────────────────────────

    /**
     * When stock is depleted (OUT_OF_STOCK), suspend all live offers for that product.
     */
    @Transactional
    public void handleInventoryEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        if (StockDepletedEvent.EVENT_TYPE.equals(envelope.getEventType())) {
            StockDepletedEvent event = objectMapper.convertValue(
                    envelope.getPayload(), StockDepletedEvent.class);
            log.info("Offer: received STOCK_DEPLETED for product: {}", event.getProductId());

            try {
                List<Offer> liveOffers = offerRepository.findLiveOffersByProductId(event.getProductId());
                for (Offer offer : liveOffers) {
                    try {
                        com.homebase.ecom.offer.service.cmds.SuspendOfferPayload payload =
                                new com.homebase.ecom.offer.service.cmds.SuspendOfferPayload();
                        payload.setReason("Out of stock");
                        payload.setComment("Auto-suspended due to stock depletion");
                        offerStateEntityService.processById(offer.getId(), "suspend", payload);
                        log.info("Suspended offer {} due to stock depletion", offer.getId());
                    } catch (Exception e) {
                        log.warn("Could not suspend offer {} for depleted product {}: {}",
                                offer.getId(), event.getProductId(), e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.error("Error processing STOCK_DEPLETED for product: {}", event.getProductId(), e);
            }
        }
    }
}

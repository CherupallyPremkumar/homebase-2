package com.homebase.ecom.returnprocessing.port;

import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;

/**
 * Outbound port for publishing return processing domain events.
 * Infrastructure layer provides Kafka implementation.
 * Domain/service layer depends only on this interface -- never on ChenilePub directly.
 */
public interface ReturnProcessingEventPublisherPort {

    /**
     * Publishes event when pickup has been scheduled for a return.
     */
    void publishPickupScheduled(ReturnProcessingSaga saga);

    /**
     * Publishes event when the returned item has been received at warehouse.
     */
    void publishItemReceived(ReturnProcessingSaga saga);

    /**
     * Publishes event when returned item inventory has been restocked.
     */
    void publishInventoryRestocked(ReturnProcessingSaga saga);

    /**
     * Publishes event when supplier settlement has been adjusted for the return.
     */
    void publishSettlementAdjusted(ReturnProcessingSaga saga);

    /**
     * Publishes event when customer refund has been processed.
     */
    void publishRefundProcessed(ReturnProcessingSaga saga);

    /**
     * Publishes event when the entire return processing saga completes successfully.
     */
    void publishReturnProcessingCompleted(ReturnProcessingSaga saga);

    /**
     * Publishes event when the return processing saga fails.
     */
    void publishReturnProcessingFailed(ReturnProcessingSaga saga, String previousState);

    /**
     * Publishes command event requesting shipping BC to schedule a return pickup.
     */
    void requestPickupScheduling(ReturnProcessingSaga saga);

    /**
     * Publishes command event requesting inventory BC to restock the returned item.
     */
    void requestInventoryRestock(ReturnProcessingSaga saga);

    /**
     * Publishes command event requesting settlement BC to adjust supplier payout.
     */
    void requestSettlementAdjustment(ReturnProcessingSaga saga);

    /**
     * Publishes command event requesting payment BC to process customer refund.
     */
    void requestRefundProcessing(ReturnProcessingSaga saga);
}

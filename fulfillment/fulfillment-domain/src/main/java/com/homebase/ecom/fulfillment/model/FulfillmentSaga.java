package com.homebase.ecom.fulfillment.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The FulfillmentSaga aggregate root. Tracks the state of the fulfillment
 * orchestration saga as it progresses through inventory reservation,
 * shipment creation, customer notification, and completion.
 */
public class FulfillmentSaga extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String orderId;
    private String userId;
    private String shipmentId;
    private String trackingNumber;
    private String errorMessage;
    private int retryCount = 0;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getShipmentId() { return shipmentId; }
    public void setShipmentId(String shipmentId) { this.shipmentId = shipmentId; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    @Override
    public TransientMap getTransientMap() { return this.transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        FulfillmentSagaActivityLog activityLog = new FulfillmentSagaActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}

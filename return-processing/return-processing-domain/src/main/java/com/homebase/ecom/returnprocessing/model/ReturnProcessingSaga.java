package com.homebase.ecom.returnprocessing.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Domain model for the return processing saga.
 * Orchestrates across: returnrequest, shipping, inventory, settlement, payment BCs.
 *
 * DB columns (return_processing_saga):
 *   Base: id, created_time, last_modified_time, last_modified_by, tenant, created_by, version
 *   STM:  state_entry_time, sla_yellow_date, sla_red_date, sla_tending_late, sla_late, flow_id, state_id
 *   Biz:  return_request_id, order_id, order_item_id, refund_amount, shipment_id,
 *         settlement_adjustment_id, refund_id, error_message, retry_count
 */
public class ReturnProcessingSaga extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String returnRequestId;
    private String orderId;
    private String orderItemId;
    private BigDecimal refundAmount;
    private String shipmentId;
    private String settlementAdjustmentId;
    private String refundId;
    private String errorMessage;
    private int retryCount;
    private String tenant;

    private transient TransientMap transientMap = new TransientMap();
    private List<ReturnProcessingSagaActivityLog> activities = new ArrayList<>();

    public String getReturnRequestId() {
        return returnRequestId;
    }

    public void setReturnRequestId(String returnRequestId) {
        this.returnRequestId = returnRequestId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getSettlementAdjustmentId() {
        return settlementAdjustmentId;
    }

    public void setSettlementAdjustmentId(String settlementAdjustmentId) {
        this.settlementAdjustmentId = settlementAdjustmentId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public List<ReturnProcessingSagaActivityLog> getActivities() {
        return activities;
    }

    public void setActivities(List<ReturnProcessingSagaActivityLog> activities) {
        this.activities = activities;
    }

    @Override
    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        Collection<ActivityLog> acts = new ArrayList<>();
        if (activities != null) {
            for (ActivityLog a : activities) {
                acts.add(a);
            }
        }
        return acts;
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ReturnProcessingSagaActivityLog activityLog = new ReturnProcessingSagaActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}

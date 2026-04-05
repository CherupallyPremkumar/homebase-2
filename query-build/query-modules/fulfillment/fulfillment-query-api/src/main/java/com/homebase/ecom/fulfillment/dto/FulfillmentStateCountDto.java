package com.homebase.ecom.fulfillment.dto;

import java.io.Serializable;

public class FulfillmentStateCountDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String stateId;
    private long count;

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
}

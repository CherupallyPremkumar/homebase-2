package com.homebase.ecom.inventory.dto;

import java.io.Serializable;

public class DamageSummaryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fulfillmentCenter;
    private String damageType;
    private String status;
    private int count;

    public String getFulfillmentCenter() { return fulfillmentCenter; }
    public void setFulfillmentCenter(String fulfillmentCenter) { this.fulfillmentCenter = fulfillmentCenter; }
    public String getDamageType() { return damageType; }
    public void setDamageType(String damageType) { this.damageType = damageType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}

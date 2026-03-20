package com.homebase.ecom.returnrequest.model;

import java.io.Serializable;

/**
 * Represents an individual item being returned as part of a return request.
 */
public class ReturnItem implements Serializable {
    private static final long serialVersionUID = 1L;

    public String orderItemId;
    public String productId;
    public String variantId;
    public int quantity = 1;
    public String reason;
    public String condition; // NEW, USED, DAMAGED, DEFECTIVE

    public ReturnItem() {}

    public ReturnItem(String orderItemId, String productId, String variantId,
                      int quantity, String reason, String condition) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.variantId = variantId;
        this.quantity = quantity;
        this.reason = reason;
        this.condition = condition;
    }

    private String tenant;
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}

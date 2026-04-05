package com.homebase.ecom.checkout.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CheckoutQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String customerId;
    private String cartId;
    private String stateId;
    private String flowId;
    private BigDecimal total;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

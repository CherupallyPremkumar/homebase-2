package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

public class MoveToWishlistPayload extends MinimalPayload {
    private static final long serialVersionUID = 1L;
    private String productId;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
}

package com.homebase.ecom.cart.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the addItem event.
 * unitPrice is in smallest currency unit (paise for INR).
 * currency is ISO 4217 code (e.g. "INR", "USD").
 */
public class AddItemCartPayload extends MinimalPayload {
    public String productId;
    public String variantId;
    public String sku;
    public String productName;
    public String supplierId;
    public int quantity;
    public long unitPrice;
    public String currency;
}

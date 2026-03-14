package com.homebase.ecom.cart.model;

/**
 * Represents the current status of a cart item,
 * reflecting its validity based on product/stock changes.
 */
public enum CartItemStatus {
    /** Item is valid and available for purchase. */
    AVAILABLE,

    /** Item is currently out of stock. */
    OUT_OF_STOCK,

    /** The price or seller of the item has changed since it was added. */
    PRICE_CHANGED
}

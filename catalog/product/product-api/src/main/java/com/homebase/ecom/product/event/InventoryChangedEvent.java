package com.homebase.ecom.product.event;

/**
 * Event published when product inventory changes.
 */
public class InventoryChangedEvent {
    private String productId;
    private int newQuantity;

    public InventoryChangedEvent() {
    }

    public InventoryChangedEvent(String productId, int newQuantity) {
        this.productId = productId;
        this.newQuantity = newQuantity;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getNewQuantity() { return newQuantity; }
    public void setNewQuantity(int newQuantity) { this.newQuantity = newQuantity; }
}

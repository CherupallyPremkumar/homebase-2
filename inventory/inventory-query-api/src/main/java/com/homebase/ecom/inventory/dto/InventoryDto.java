package com.homebase.ecom.inventory.dto;

/**
 * Canonical response DTO for the "inventories" and "inventory" queries.
 *
 * Field names MUST match the SQL column aliases defined in inventory.xml.
 */
public class InventoryDto {

    private String id;
    private String productId;
    private Integer quantity;
    private Integer reserved;
    private Integer lowStockThreshold;
    private String status;
    private String stateId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getReserved() { return reserved; }
    public void setReserved(Integer reserved) { this.reserved = reserved; }

    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
}

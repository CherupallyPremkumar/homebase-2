package com.homebase.ecom.inventory.dto;

/**
 * Canonical response DTO for the "inventories" and "inventory" queries.
 *
 * Field names MUST match the SQL column aliases defined in inventory.xml.
 */
public class InventoryDto {

    private String id;
    private String sku;
    private String asin;
    private String productId;
    private String variantId;
    private Integer quantity;
    private Integer availableQuantity;
    private Integer reserved;
    private Integer damagedQuantity;
    private Integer inboundQuantity;
    private Integer lowStockThreshold;
    private String status;
    private String primaryFc;
    private Boolean isFba;
    private Boolean isMerchantFulfilled;
    private String stateId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getAsin() { return asin; }
    public void setAsin(String asin) { this.asin = asin; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    public Integer getReserved() { return reserved; }
    public void setReserved(Integer reserved) { this.reserved = reserved; }

    public Integer getDamagedQuantity() { return damagedQuantity; }
    public void setDamagedQuantity(Integer damagedQuantity) { this.damagedQuantity = damagedQuantity; }

    public Integer getInboundQuantity() { return inboundQuantity; }
    public void setInboundQuantity(Integer inboundQuantity) { this.inboundQuantity = inboundQuantity; }

    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPrimaryFc() { return primaryFc; }
    public void setPrimaryFc(String primaryFc) { this.primaryFc = primaryFc; }

    public Boolean getIsFba() { return isFba; }
    public void setIsFba(Boolean isFba) { this.isFba = isFba; }

    public Boolean getIsMerchantFulfilled() { return isMerchantFulfilled; }
    public void setIsMerchantFulfilled(Boolean isMerchantFulfilled) { this.isMerchantFulfilled = isMerchantFulfilled; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
}

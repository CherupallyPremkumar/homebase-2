package com.homebase.ecom.wms.dto;

import java.io.Serializable;
import java.util.Date;

public class InventoryByLocationQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String warehouseInventoryId;
    private String locationId;
    private String productId;
    private String variantId;
    private String sku;
    private int quantityOnHand;
    private int quantityReserved;
    private int quantityAvailable;
    private int quantityDamaged;
    private int quantityInTransit;
    private Date lastCountedAt;
    private String locationCode;
    private String zone;
    private String aisle;
    private String shelf;
    private String bin;
    private String locationType;
    private int locationCapacity;
    private int locationCurrentUnits;
    private String productName;
    private String brand;
    private String warehouseName;
    private String warehouseCode;

    public String getWarehouseInventoryId() { return warehouseInventoryId; }
    public void setWarehouseInventoryId(String warehouseInventoryId) { this.warehouseInventoryId = warehouseInventoryId; }
    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(int quantityOnHand) { this.quantityOnHand = quantityOnHand; }
    public int getQuantityReserved() { return quantityReserved; }
    public void setQuantityReserved(int quantityReserved) { this.quantityReserved = quantityReserved; }
    public int getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(int quantityAvailable) { this.quantityAvailable = quantityAvailable; }
    public int getQuantityDamaged() { return quantityDamaged; }
    public void setQuantityDamaged(int quantityDamaged) { this.quantityDamaged = quantityDamaged; }
    public int getQuantityInTransit() { return quantityInTransit; }
    public void setQuantityInTransit(int quantityInTransit) { this.quantityInTransit = quantityInTransit; }
    public Date getLastCountedAt() { return lastCountedAt; }
    public void setLastCountedAt(Date lastCountedAt) { this.lastCountedAt = lastCountedAt; }
    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    public String getAisle() { return aisle; }
    public void setAisle(String aisle) { this.aisle = aisle; }
    public String getShelf() { return shelf; }
    public void setShelf(String shelf) { this.shelf = shelf; }
    public String getBin() { return bin; }
    public void setBin(String bin) { this.bin = bin; }
    public String getLocationType() { return locationType; }
    public void setLocationType(String locationType) { this.locationType = locationType; }
    public int getLocationCapacity() { return locationCapacity; }
    public void setLocationCapacity(int locationCapacity) { this.locationCapacity = locationCapacity; }
    public int getLocationCurrentUnits() { return locationCurrentUnits; }
    public void setLocationCurrentUnits(int locationCurrentUnits) { this.locationCurrentUnits = locationCurrentUnits; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
}

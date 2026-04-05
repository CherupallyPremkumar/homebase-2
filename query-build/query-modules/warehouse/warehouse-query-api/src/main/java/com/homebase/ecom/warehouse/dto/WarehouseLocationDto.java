package com.homebase.ecom.warehouse.dto;

import java.io.Serializable;

public class WarehouseLocationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String warehouseId;
    private String locationCode;
    private String zone;
    private String aisle;
    private String shelf;
    private String bin;
    private String locationType;
    private int capacityUnits;
    private int currentUnits;
    private boolean isActive;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
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
    public int getCapacityUnits() { return capacityUnits; }
    public void setCapacityUnits(int capacityUnits) { this.capacityUnits = capacityUnits; }
    public int getCurrentUnits() { return currentUnits; }
    public void setCurrentUnits(int currentUnits) { this.currentUnits = currentUnits; }
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
}

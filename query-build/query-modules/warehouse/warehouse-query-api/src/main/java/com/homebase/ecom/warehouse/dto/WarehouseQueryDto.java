package com.homebase.ecom.warehouse.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class WarehouseQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseType;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private String contactPhone;
    private String contactEmail;
    private String managerId;
    private int maxCapacityUnits;
    private BigDecimal currentUtilizationPct;
    private boolean isActive;
    private boolean isReturnsCenter;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseType() { return warehouseType; }
    public void setWarehouseType(String warehouseType) { this.warehouseType = warehouseType; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }
    public int getMaxCapacityUnits() { return maxCapacityUnits; }
    public void setMaxCapacityUnits(int maxCapacityUnits) { this.maxCapacityUnits = maxCapacityUnits; }
    public BigDecimal getCurrentUtilizationPct() { return currentUtilizationPct; }
    public void setCurrentUtilizationPct(BigDecimal currentUtilizationPct) { this.currentUtilizationPct = currentUtilizationPct; }
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public boolean getIsReturnsCenter() { return isReturnsCenter; }
    public void setIsReturnsCenter(boolean isReturnsCenter) { this.isReturnsCenter = isReturnsCenter; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}

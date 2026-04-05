package com.homebase.ecom.warehouse.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class WarehouseDetailDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String timezone;
    private String contactPhone;
    private String contactEmail;
    private String managerId;
    private int maxCapacityUnits;
    private BigDecimal currentUtilizationPct;
    private boolean isActive;
    private boolean isReturnsCenter;
    private String operatingHoursJson;
    private String supportedCarriersJson;
    private Date createdTime;
    private Date lastModifiedTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseType() { return warehouseType; }
    public void setWarehouseType(String warehouseType) { this.warehouseType = warehouseType; }
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
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
    public String getOperatingHoursJson() { return operatingHoursJson; }
    public void setOperatingHoursJson(String operatingHoursJson) { this.operatingHoursJson = operatingHoursJson; }
    public String getSupportedCarriersJson() { return supportedCarriersJson; }
    public void setSupportedCarriersJson(String supportedCarriersJson) { this.supportedCarriersJson = supportedCarriersJson; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(Date lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
}

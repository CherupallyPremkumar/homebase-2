package com.homebase.ecom.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class HeldFundsDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String supplierId;
    private BigDecimal heldAmount;
    private Integer itemCount;

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public BigDecimal getHeldAmount() { return heldAmount; }
    public void setHeldAmount(BigDecimal heldAmount) { this.heldAmount = heldAmount; }
    public Integer getItemCount() { return itemCount; }
    public void setItemCount(Integer itemCount) { this.itemCount = itemCount; }
}

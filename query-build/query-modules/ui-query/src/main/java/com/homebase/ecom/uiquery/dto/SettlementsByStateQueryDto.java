package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SettlementsByStateQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String state;
    private long count;
    private BigDecimal totalAmount;

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}

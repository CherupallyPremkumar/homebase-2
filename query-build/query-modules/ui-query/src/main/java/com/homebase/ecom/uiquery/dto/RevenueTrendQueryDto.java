package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RevenueTrendQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date date;
    private BigDecimal revenue;
    private long orderCount;

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
    public long getOrderCount() { return orderCount; }
    public void setOrderCount(long orderCount) { this.orderCount = orderCount; }
}

package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DailyOrderStatsQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date date;
    private long orderCount;
    private BigDecimal revenue;

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public long getOrderCount() { return orderCount; }
    public void setOrderCount(long orderCount) { this.orderCount = orderCount; }
    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
}

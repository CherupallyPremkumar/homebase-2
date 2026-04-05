package com.homebase.ecom.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DailyRevenueDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date paymentDate;
    private long transactionCount;
    private BigDecimal totalRevenue;
    private String currency;

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    public long getTransactionCount() { return transactionCount; }
    public void setTransactionCount(long transactionCount) { this.transactionCount = transactionCount; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}

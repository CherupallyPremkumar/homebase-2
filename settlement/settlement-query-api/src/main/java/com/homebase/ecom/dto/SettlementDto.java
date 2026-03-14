package com.homebase.ecom.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for Settlement query responses.
 */
public class SettlementDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String supplierId;
    private Integer periodMonth;
    private Integer periodYear;
    private String status;
    private String stateId;
    private BigDecimal totalSalesAmount;
    private BigDecimal commissionAmount;
    private BigDecimal netPayoutAmount;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public Integer getPeriodMonth() { return periodMonth; }
    public void setPeriodMonth(Integer periodMonth) { this.periodMonth = periodMonth; }
    public Integer getPeriodYear() { return periodYear; }
    public void setPeriodYear(Integer periodYear) { this.periodYear = periodYear; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public BigDecimal getTotalSalesAmount() { return totalSalesAmount; }
    public void setTotalSalesAmount(BigDecimal totalSalesAmount) { this.totalSalesAmount = totalSalesAmount; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    public BigDecimal getNetPayoutAmount() { return netPayoutAmount; }
    public void setNetPayoutAmount(BigDecimal netPayoutAmount) { this.netPayoutAmount = netPayoutAmount; }
}

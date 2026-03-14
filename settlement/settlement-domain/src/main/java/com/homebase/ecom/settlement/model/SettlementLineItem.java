package com.homebase.ecom.settlement.model;

import com.homebase.ecom.shared.Money;

public class SettlementLineItem {

    private String id;
    private String orderId;
    private String orderItemId;
    private Money itemSalesAmount;
    private Money itemCommissionAmount;
    private transient Settlement settlement;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderItemId() { return orderItemId; }
    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }

    public Money getItemSalesAmount() { return itemSalesAmount; }
    public void setItemSalesAmount(Money itemSalesAmount) { this.itemSalesAmount = itemSalesAmount; }

    public Money getItemCommissionAmount() { return itemCommissionAmount; }
    public void setItemCommissionAmount(Money itemCommissionAmount) { this.itemCommissionAmount = itemCommissionAmount; }

    public Settlement getSettlement() { return settlement; }
    public void setSettlement(Settlement settlement) { this.settlement = settlement; }
}

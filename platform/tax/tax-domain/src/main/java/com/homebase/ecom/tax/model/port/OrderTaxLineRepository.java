package com.homebase.ecom.tax.model.port;

import com.homebase.ecom.tax.model.OrderTaxLine;

import java.util.List;

public interface OrderTaxLineRepository {

    List<OrderTaxLine> findByOrderId(String orderId);

    OrderTaxLine save(OrderTaxLine orderTaxLine);
}

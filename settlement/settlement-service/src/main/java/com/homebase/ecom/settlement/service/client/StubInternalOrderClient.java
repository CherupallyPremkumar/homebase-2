package com.homebase.ecom.settlement.service.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.homebase.ecom.settlement.dto.SettlementOrderItemDTO;
import org.springframework.stereotype.Service;

@Service
public class StubInternalOrderClient implements InternalOrderClient {

    @Override
    public List<SettlementOrderItemDTO> getDeliveredOrderItemsForSupplier(String supplierId, int month, int year) {
        // Stub implementation for compilation and demonstration purposes.
        // Replace with actual HTTP/Feign call to Order Service.
        List<SettlementOrderItemDTO> items = new ArrayList<>();

        SettlementOrderItemDTO item1 = new SettlementOrderItemDTO();
        item1.orderId = "mock-order-1";
        item1.orderItemId = "mock-item-1";
        item1.itemSalesAmount = new BigDecimal("5000.00"); // e.g. 5000 INR for a saree
        items.add(item1);

        return items;
    }
}

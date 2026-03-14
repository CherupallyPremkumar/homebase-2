package com.homebase.ecom.settlement.service.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.homebase.ecom.settlement.dto.SettlementOrderItemDTO;
import org.springframework.stereotype.Service;

/**
 * Stub implementation of InternalOrderClient for testing and development.
 * Returns configurable mock data for settlement calculations.
 */
@Service
public class StubInternalOrderClient implements InternalOrderClient {

    private List<SettlementOrderItemDTO> stubbedItems = null;

    /**
     * Allows tests to configure what items this stub returns.
     */
    public void setItems(List<SettlementOrderItemDTO> items) {
        this.stubbedItems = items;
    }

    /**
     * Resets stub to default behavior.
     */
    public void reset() {
        this.stubbedItems = null;
    }

    @Override
    public List<SettlementOrderItemDTO> getDeliveredOrderItemsForSupplier(String supplierId, int month, int year) {
        if (stubbedItems != null) {
            return stubbedItems;
        }

        // Default stub: 3 items totaling 15000 INR for BDD test compatibility
        List<SettlementOrderItemDTO> items = new ArrayList<>();

        SettlementOrderItemDTO item1 = new SettlementOrderItemDTO();
        item1.orderId = "order-001";
        item1.orderItemId = "item-001";
        item1.itemSalesAmount = new BigDecimal("5000.00");
        items.add(item1);

        SettlementOrderItemDTO item2 = new SettlementOrderItemDTO();
        item2.orderId = "order-002";
        item2.orderItemId = "item-002";
        item2.itemSalesAmount = new BigDecimal("5000.00");
        items.add(item2);

        SettlementOrderItemDTO item3 = new SettlementOrderItemDTO();
        item3.orderId = "order-003";
        item3.orderItemId = "item-003";
        item3.itemSalesAmount = new BigDecimal("5000.00");
        items.add(item3);

        return items;
    }
}

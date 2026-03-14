package com.homebase.ecom.settlement.service.client;

import com.homebase.ecom.order.repository.OrderRepository;
import com.homebase.ecom.settlement.dto.SettlementOrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Real implementation of InternalOrderClient that uses OrderQueryAdapter.
 */
@Service
@Primary
public class InternalOrderClientImpl implements InternalOrderClient {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<SettlementOrderItemDTO> getDeliveredOrderItemsForSupplier(String supplierId, int month, int year) {
        List<Map<String, Object>> results = orderRepository.getDeliveredOrderItems(supplierId, month, year);

        return results.stream().map(row -> {
            SettlementOrderItemDTO dto = new SettlementOrderItemDTO();
            dto.orderId = (String) row.get("orderId");
            dto.orderItemId = (String) row.get("orderItemId");

            Object amount = row.get("itemSalesAmount");
            if (amount instanceof BigDecimal) {
                dto.itemSalesAmount = (BigDecimal) amount;
            } else if (amount instanceof Number) {
                dto.itemSalesAmount = new BigDecimal(amount.toString());
            }
            return dto;
        }).collect(Collectors.toList());
    }
}

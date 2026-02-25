package com.homebase.ecom.settlement.service.client;

import java.util.List;
import com.homebase.ecom.settlement.dto.SettlementOrderItemDTO;

/**
 * Internal client for communicating with the Order Bounded Context.
 * Implementation should use Feign or RestTemplate depending on the service
 * mesh.
 */
public interface InternalOrderClient {
    List<SettlementOrderItemDTO> getDeliveredOrderItemsForSupplier(String supplierId, int month, int year);
}

package com.homebase.ecom.returnrequest.domain.port;

import com.homebase.ecom.returnrequest.model.ReturnItem;
import java.util.List;

/**
 * Port for updating inventory when returned items are processed.
 * Adapter connects to the inventory service.
 */
public interface InventoryPort {
    /**
     * Notifies inventory service that returned items should be restocked.
     * @param returnRequestId the return request ID
     * @param items the items being returned
     * @param warehouseId the warehouse receiving the items
     */
    void restockReturnedItems(String returnRequestId, List<ReturnItem> items, String warehouseId);
}

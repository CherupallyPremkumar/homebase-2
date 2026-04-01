package com.homebase.ecom.order.repository;

import com.homebase.ecom.order.model.Order;
import org.chenile.query.annotation.ChenileParam;
import org.chenile.query.annotation.QueryName;
import org.chenile.query.repository.ChenileRepository;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for fetching Order details.
 */
public interface OrderRepository extends ChenileRepository<Order> {
    @QueryName("Order.getDeliveredOrderItems")
    List<Map<String, Object>> getDeliveredOrderItems(
            @ChenileParam("supplierId") String supplierId,
            @ChenileParam("month") int month,
            @ChenileParam("year") int year);
}

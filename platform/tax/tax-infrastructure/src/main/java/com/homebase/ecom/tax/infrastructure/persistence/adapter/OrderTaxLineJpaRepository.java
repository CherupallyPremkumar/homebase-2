package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.infrastructure.persistence.entity.OrderTaxLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderTaxLineJpaRepository extends JpaRepository<OrderTaxLineEntity, String> {
    List<OrderTaxLineEntity> findByOrderId(String orderId);
}

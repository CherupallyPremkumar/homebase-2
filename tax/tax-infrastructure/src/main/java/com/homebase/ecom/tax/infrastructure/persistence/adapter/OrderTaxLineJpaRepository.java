package com.homebase.ecom.tax.infrastructure.persistence.adapter;

import com.homebase.ecom.tax.infrastructure.persistence.entity.OrderTaxLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTaxLineJpaRepository extends JpaRepository<OrderTaxLineEntity, String> {

    List<OrderTaxLineEntity> findByOrderId(String orderId);
}

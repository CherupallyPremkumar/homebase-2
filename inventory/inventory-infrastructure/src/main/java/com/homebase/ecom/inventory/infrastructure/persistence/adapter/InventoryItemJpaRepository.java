package com.homebase.ecom.inventory.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemEntity;

public interface InventoryItemJpaRepository extends JpaRepository<InventoryItemEntity, String> {
    Optional<InventoryItemEntity> findByProductId(String productId);

    @Query("SELECT i FROM InventoryItemEntity i JOIN i.reservations r WHERE r.orderId = :orderId")
    List<InventoryItemEntity> findByOrderIdInReservations(@Param("orderId") String orderId);

    @Query("SELECT COUNT(i) > 0 FROM InventoryItemEntity i JOIN i.reservations r WHERE r.orderId = :orderId")
    boolean existsByOrderIdInReservations(@Param("orderId") String orderId);
}

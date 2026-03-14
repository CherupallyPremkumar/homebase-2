package com.homebase.ecom.cart.infrastructure.persistence.repository;

import com.homebase.ecom.cart.infrastructure.persistence.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, String> {

    @Modifying
    @Query("UPDATE CartItemEntity ci SET ci.priceAmount = :price, ci.priceCurrency = :currency, ci.sellerId = :sellerId, ci.status = :status WHERE ci.productId = :productId")
    void updatePriceAndSeller(
            @Param("productId") String productId,
            @Param("price") BigDecimal price,
            @Param("currency") String currency,
            @Param("sellerId") String sellerId,
            @Param("status") String status);

    @Modifying
    @Query("UPDATE CartItemEntity ci SET ci.status = :status WHERE ci.productId = :productId")
    void updateStatusByProductId(
            @Param("productId") String productId,
            @Param("status") String status);
}

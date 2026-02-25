package com.homebase.ecom.inventory.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stock_reservation_items")
@IdClass(StockReservationItem.StockReservationItemId.class)
public class StockReservationItem implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private StockReservation reservation;

    @Id
    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(nullable = false)
    private Integer quantity;

    // --- Getters & Setters ---

    public StockReservation getReservation() {
        return reservation;
    }

    public void setReservation(StockReservation reservation) {
        this.reservation = reservation;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public static class StockReservationItemId implements Serializable {
        private String reservation;
        private String productId;

        // Default constructor, equals, and hashCode
        public StockReservationItemId() {
        }

        public StockReservationItemId(String reservation, String productId) {
            this.reservation = reservation;
            this.productId = productId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            StockReservationItemId that = (StockReservationItemId) o;
            return java.util.Objects.equals(reservation, that.reservation) &&
                    java.util.Objects.equals(productId, that.productId);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(reservation, productId);
        }
    }
}

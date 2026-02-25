package com.homebase.ecom.inventory.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_reservations")
public class StockReservation extends BaseJpaEntity {

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<StockReservationItem> items = new ArrayList<>();

    // --- Getters & Setters ---

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<StockReservationItem> getItems() {
        return items;
    }

    public void setItems(List<StockReservationItem> items) {
        this.items = items;
    }

    public void addItem(StockReservationItem item) {
        items.add(item);
        item.setReservation(this);
    }
}

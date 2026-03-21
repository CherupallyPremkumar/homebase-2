package com.homebase.ecom.demoorder.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

@Entity
@Table(name = "demo_orders")
public class DemoOrderEntity extends AbstractJpaStateEntity implements ContainsTransientMap {

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "customer_id")
    private String customerId;

    @Transient
    private TransientMap transientMap = new TransientMap();

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }
}

package com.homebase.ecom.demoorder.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

/**
 * Domain model for a demo order.
 * Simple entity with productName, quantity, customerId.
 * No JPA annotations -- domain purity.
 */
public class DemoOrder extends AbstractExtendedStateEntity implements ContainsTransientMap {

    private String productName;
    private int quantity;
    private String customerId;

    private transient TransientMap transientMap = new TransientMap();

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }
}

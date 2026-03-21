package com.homebase.ecom.demonotification.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

/**
 * Domain model for a demo notification.
 * Created automatically when a DemoOrder is processed in demo-order.
 * No JPA annotations -- domain purity.
 */
public class DemoNotification extends AbstractExtendedStateEntity implements ContainsTransientMap {

    private String orderId;
    private String message;
    private String channel;

    private transient TransientMap transientMap = new TransientMap();

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }
}

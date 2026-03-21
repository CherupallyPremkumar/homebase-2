package com.homebase.ecom.demonotification.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

@Entity
@Table(name = "demo_notifications")
public class DemoNotificationEntity extends AbstractJpaStateEntity implements ContainsTransientMap {

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "message")
    private String message;

    @Column(name = "channel")
    private String channel;

    @Transient
    private TransientMap transientMap = new TransientMap();

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }
}

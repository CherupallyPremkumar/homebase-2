package com.homebase.ecom.notification.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.BaseJpaEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "notification_preferences")
public class NotificationPreferenceEntity extends BaseJpaEntity {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "channel")
    private String channel;

    @Column(name = "category")
    private String category;

    @Column(name = "enabled")
    private boolean enabled = true;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}

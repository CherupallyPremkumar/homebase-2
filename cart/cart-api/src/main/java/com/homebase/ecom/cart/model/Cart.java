package com.homebase.ecom.cart.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import com.homebase.ecom.cart.model.CartActivityLog;
import org.chenile.workflow.model.*;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

@Entity
@Table(name = "cart")
public class Cart extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {
    @Column(name = "user_id")
    private String userId;

    public String getStatus() {
        return getCurrentState() != null ? getCurrentState().getStateId() : null;
    }

    public void setStatus(String status) {
        if (getCurrentState() == null) {
            setCurrentState(new org.chenile.stm.State());
        }
        getCurrentState().setStateId(status);
    }

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public String description;
    @Transient
    public TransientMap transientMap = new TransientMap();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    public List<CartActivityLog> activities = new ArrayList<>();

    @Override
    public Collection<ActivityLog> obtainActivities() {
        Collection<ActivityLog> acts = new ArrayList<>();
        for (ActivityLog a : activities) {
            acts.add(a);
        }
        return acts;
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        CartActivityLog activityLog = new CartActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}

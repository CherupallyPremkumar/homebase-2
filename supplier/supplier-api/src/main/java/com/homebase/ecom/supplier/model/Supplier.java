package com.homebase.ecom.supplier.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import com.homebase.ecom.supplier.model.SupplierActivityLog;
import org.chenile.workflow.model.*;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

@Entity
@Table(name = "supplier")
public class Supplier extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {
    @Column(nullable = false)
    private String name;

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

    private String email;

    public String description;
    @Transient
    public TransientMap transientMap = new TransientMap();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "supplier_id")
    public List<SupplierActivityLog> activities = new ArrayList<>();

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
        SupplierActivityLog activityLog = new SupplierActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}

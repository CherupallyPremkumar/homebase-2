package com.homebase.ecom.rulesengine.infrastructure.persistence.entity;

import com.homebase.ecom.rulesengine.api.enums.Effect;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "policy")
public class RuleSetEntity extends AbstractJpaStateEntity implements ActivityEnabledStateEntity, ContainsTransientMap {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private List<RuleSetActivityLogEntity> activities = new ArrayList<>();

    @Transient
    private TransientMap transientMap = new TransientMap();

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_effect")
    private Effect defaultEffect;

    @Column(name = "active")
    private boolean active;

    @Column(name = "target_module")
    private String targetModule;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ruleSet", orphanRemoval = true)
    private List<RuleEntity> rules = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Effect getDefaultEffect() {
        return defaultEffect;
    }

    public void setDefaultEffect(Effect defaultEffect) {
        this.defaultEffect = defaultEffect;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<RuleEntity> getRules() {
        return rules;
    }

    public void setRules(List<RuleEntity> rules) {
        this.rules = rules;
    }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        RuleSetActivityLogEntity activityLog = new RuleSetActivityLogEntity();
        activityLog.setName(eventId);
        activityLog.setComment(comment);
        activityLog.setSuccess(true);
        activities.add(activityLog);
        return activityLog;
    }

    public List<RuleSetActivityLogEntity> getActivities() {
        return activities;
    }

    public void setActivities(List<RuleSetActivityLogEntity> activities) {
        this.activities = activities;
    }

    @Override
    public TransientMap getTransientMap() {
        return transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    public String getTargetModule() {
        return targetModule;
    }

    public void setTargetModule(String targetModule) {
        this.targetModule = targetModule;
    }
}

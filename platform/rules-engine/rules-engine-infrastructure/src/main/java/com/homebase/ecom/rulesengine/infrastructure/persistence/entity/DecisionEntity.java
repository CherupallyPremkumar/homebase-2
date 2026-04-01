package com.homebase.ecom.rulesengine.infrastructure.persistence.entity;

import com.homebase.ecom.rulesengine.api.enums.Effect;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "decisions")
public class DecisionEntity extends BaseJpaEntity {

    @Column(name = "policy_id")
    private String ruleSetId;

    @Column(name = "subject_id")
    private String subjectId;

    @Column(name = "resource")
    private String resource;
    private String action;

    @Enumerated(EnumType.STRING)
    private Effect effect;

    private String reasons;

    @Column(name = "target_module")
    private String targetModule;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "decision_metadata", joinColumns = @JoinColumn(name = "decision_id"))
    @MapKeyColumn(name = "meta_key")
    @Column(name = "meta_value")
    private Map<String, String> metadata = new HashMap<>();

    public String getRuleSetId() {
        return ruleSetId;
    }

    public void setRuleSetId(String ruleSetId) {
        this.ruleSetId = ruleSetId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getTargetModule() {
        return targetModule;
    }

    public void setTargetModule(String targetModule) {
        this.targetModule = targetModule;
    }
}

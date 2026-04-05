package com.homebase.ecom.rulesengine.query.dto;

public class RuleQueryDto {
    private String id;
    private String name;
    private String expression;
    private String effect;
    private int priority;
    private String policyId;
    private String policyName;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
    public String getEffect() { return effect; }
    public void setEffect(String effect) { this.effect = effect; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }
    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }
}

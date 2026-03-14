package com.homebase.ecom.policy.api.dto;

import java.io.Serializable;
import java.util.Map;

public class ContextDto implements Serializable {
    private String tenantId;
    private String userId;
    private Map<String, Object> data;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}

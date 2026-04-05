package com.homebase.ecom.clickstream.dto;

import java.io.Serializable;
import java.util.Date;

public class ClickEventQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String sessionId;
    private String userId;
    private String elementType;
    private String elementId;
    private String pageUrl;
    private String eventDataJson;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getElementType() { return elementType; }
    public void setElementType(String elementType) { this.elementType = elementType; }
    public String getElementId() { return elementId; }
    public void setElementId(String elementId) { this.elementId = elementId; }
    public String getPageUrl() { return pageUrl; }
    public void setPageUrl(String pageUrl) { this.pageUrl = pageUrl; }
    public String getEventDataJson() { return eventDataJson; }
    public void setEventDataJson(String eventDataJson) { this.eventDataJson = eventDataJson; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}

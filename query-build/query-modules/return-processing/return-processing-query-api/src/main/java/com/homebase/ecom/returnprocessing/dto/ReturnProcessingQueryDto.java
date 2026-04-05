package com.homebase.ecom.returnprocessing.dto;

import java.io.Serializable;
import java.util.Date;

public class ReturnProcessingQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String returnRequestId;
    private String stateId;
    private String flowId;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getReturnRequestId() { return returnRequestId; }
    public void setReturnRequestId(String returnRequestId) { this.returnRequestId = returnRequestId; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}

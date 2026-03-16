package com.homebase.ecom.dto;

import java.io.Serializable;

public class StateCountDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String stateId;
    private int count;

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}

package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;

/**
 * Shared DTO for queries that return state + count groupings.
 * Used by: ordersByState, returnsByState, customerSummary.
 */
public class StateCountQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String state;
    private long count;

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
}

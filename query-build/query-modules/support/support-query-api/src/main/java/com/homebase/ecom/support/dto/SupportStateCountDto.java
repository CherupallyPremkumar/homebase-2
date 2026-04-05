package com.homebase.ecom.support.dto;

import java.io.Serializable;

/**
 * Query DTO for the getStateCounts aggregation query, returning
 * ticket counts grouped by state.
 */
public class SupportStateCountDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String stateId;
    private long ticketCount;

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public long getTicketCount() { return ticketCount; }
    public void setTicketCount(long ticketCount) { this.ticketCount = ticketCount; }
}

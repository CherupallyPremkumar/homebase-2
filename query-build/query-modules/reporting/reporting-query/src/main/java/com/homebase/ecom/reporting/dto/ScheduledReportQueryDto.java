package com.homebase.ecom.reporting.dto;

import java.io.Serializable;
import java.util.Date;

public class ScheduledReportQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String reportDefinitionId;
    private String reportName;
    private String format;
    private String frequency;
    private String cronExpression;
    private Date nextRunAt;
    private Date lastRunAt;
    private String recipientEmail;
    private String status;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getReportDefinitionId() { return reportDefinitionId; }
    public void setReportDefinitionId(String reportDefinitionId) { this.reportDefinitionId = reportDefinitionId; }
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    public Date getNextRunAt() { return nextRunAt; }
    public void setNextRunAt(Date nextRunAt) { this.nextRunAt = nextRunAt; }
    public Date getLastRunAt() { return lastRunAt; }
    public void setLastRunAt(Date lastRunAt) { this.lastRunAt = lastRunAt; }
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}

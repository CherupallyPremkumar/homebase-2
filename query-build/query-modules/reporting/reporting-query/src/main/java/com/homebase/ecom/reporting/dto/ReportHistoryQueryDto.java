package com.homebase.ecom.reporting.dto;

import java.io.Serializable;
import java.util.Date;

public class ReportHistoryQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String reportDefinitionId;
    private String reportDefinitionName;
    private String name;
    private String format;
    private String generatedBy;
    private String fileUrl;
    private long fileSizeBytes;
    private int recordCount;
    private String status;
    private Date completedAt;
    private String errorMessage;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getReportDefinitionId() { return reportDefinitionId; }
    public void setReportDefinitionId(String reportDefinitionId) { this.reportDefinitionId = reportDefinitionId; }
    public String getReportDefinitionName() { return reportDefinitionName; }
    public void setReportDefinitionName(String reportDefinitionName) { this.reportDefinitionName = reportDefinitionName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public int getRecordCount() { return recordCount; }
    public void setRecordCount(int recordCount) { this.recordCount = recordCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCompletedAt() { return completedAt; }
    public void setCompletedAt(Date completedAt) { this.completedAt = completedAt; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}

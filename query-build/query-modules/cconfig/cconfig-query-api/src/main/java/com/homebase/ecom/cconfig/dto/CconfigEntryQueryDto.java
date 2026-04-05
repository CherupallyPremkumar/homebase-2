package com.homebase.ecom.cconfig.dto;

import java.io.Serializable;
import java.util.Date;

public class CconfigEntryQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String moduleName;
    private String keyName;
    private String avalue;
    private String customAttribute;
    private String tenant;
    private Date createdTime;
    private Date lastModifiedTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public String getKeyName() { return keyName; }
    public void setKeyName(String keyName) { this.keyName = keyName; }
    public String getAvalue() { return avalue; }
    public void setAvalue(String avalue) { this.avalue = avalue; }
    public String getCustomAttribute() { return customAttribute; }
    public void setCustomAttribute(String customAttribute) { this.customAttribute = customAttribute; }
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(Date lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
}

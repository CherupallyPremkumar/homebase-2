package com.homebase.ecom.compliance.model;

import org.chenile.workflow.activities.model.ActivityLog;

public class PlatformPolicyActivity implements ActivityLog {

    private String name;
    private boolean success;
    private String comment;

    public PlatformPolicyActivity() {}

    public PlatformPolicyActivity(String name, String comment) {
        this.name = name;
        this.comment = comment;
        this.success = true;
    }

    @Override
    public String getName() { return name; }

    @Override
    public boolean getSuccess() { return success; }

    @Override
    public String getComment() { return comment; }

    public void setName(String name) { this.name = name; }
    public void setSuccess(boolean success) { this.success = success; }
    public void setComment(String comment) { this.comment = comment; }
}

package com.homebase.ecom.onboarding.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for completeTraining event — seller completes a training module.
 */
public class CompleteTrainingPayload extends MinimalPayload {
    private String moduleId;

    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }
}

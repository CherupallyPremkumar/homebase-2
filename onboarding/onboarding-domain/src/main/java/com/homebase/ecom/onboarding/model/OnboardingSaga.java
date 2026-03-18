package com.homebase.ecom.onboarding.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

/**
 * Onboarding domain entity representing a supplier onboarding application.
 * Tracks the full lifecycle from APPLICATION_SUBMITTED through to COMPLETED.
 */
public class OnboardingSaga extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String id;
    private String supplierId;
    private String applicantName;
    private String businessName;
    private String businessType;
    private List<OnboardingDocument> documents = new ArrayList<>();
    private String verificationNotes;
    private int trainingProgress;
    private List<String> trainingCompletedModules = new ArrayList<>();
    private int resubmissionCount;
    private LocalDateTime submittedAt = LocalDateTime.now();
    private String rejectionReason;
    private String tenant;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public List<OnboardingDocument> getDocuments() { return documents; }
    public void setDocuments(List<OnboardingDocument> documents) { this.documents = documents != null ? documents : new ArrayList<>(); }

    public String getVerificationNotes() { return verificationNotes; }
    public void setVerificationNotes(String verificationNotes) { this.verificationNotes = verificationNotes; }

    public int getTrainingProgress() { return trainingProgress; }
    public void setTrainingProgress(int trainingProgress) { this.trainingProgress = trainingProgress; }

    public List<String> getTrainingCompletedModules() { return trainingCompletedModules; }
    public void setTrainingCompletedModules(List<String> trainingCompletedModules) {
        this.trainingCompletedModules = trainingCompletedModules != null ? trainingCompletedModules : new ArrayList<>();
    }

    public int getResubmissionCount() { return resubmissionCount; }
    public void setResubmissionCount(int resubmissionCount) { this.resubmissionCount = resubmissionCount; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    // ── Computed helpers ──

    /**
     * Checks if all required documents have been submitted and verified.
     */
    public boolean areAllDocumentsVerified() {
        if (documents == null || documents.isEmpty()) return false;
        return documents.stream().allMatch(OnboardingDocument::isVerified);
    }

    /**
     * Returns the number of days since the application was submitted.
     * Used by auto-state CHECK_TIMEOUT.
     */
    public long getDaysSinceSubmission() {
        if (submittedAt == null) return 0;
        return java.time.Duration.between(submittedAt, LocalDateTime.now()).toDays();
    }

    /**
     * Returns the number of training modules completed.
     * Used by auto-state CHECK_TRAINING_COMPLETE.
     */
    public int getCompletedModuleCount() {
        return trainingCompletedModules != null ? trainingCompletedModules.size() : 0;
    }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        OnboardingSagaActivityLog activityLog = new OnboardingSagaActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        this.activities.add(activityLog);
        return activityLog;
    }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}

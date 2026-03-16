package com.homebase.ecom.onboarding.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "supplier_onboarding")
public class OnboardingSagaEntity extends AbstractJpaStateEntity {

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "applicant_name")
    private String applicantName;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "documents_json", columnDefinition = "TEXT")
    private String documentsJson;

    @Column(name = "verification_notes", columnDefinition = "TEXT")
    private String verificationNotes;

    @Column(name = "training_progress")
    private int trainingProgress;

    @Column(name = "training_completed_modules_json", columnDefinition = "TEXT")
    private String trainingCompletedModulesJson;

    @Column(name = "resubmission_count")
    private int resubmissionCount;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "onboarding_id")
    private List<OnboardingSagaActivityLogEntity> activities = new ArrayList<>();

    // Getters and Setters
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getDocumentsJson() { return documentsJson; }
    public void setDocumentsJson(String documentsJson) { this.documentsJson = documentsJson; }

    public String getVerificationNotes() { return verificationNotes; }
    public void setVerificationNotes(String verificationNotes) { this.verificationNotes = verificationNotes; }

    public int getTrainingProgress() { return trainingProgress; }
    public void setTrainingProgress(int trainingProgress) { this.trainingProgress = trainingProgress; }

    public String getTrainingCompletedModulesJson() { return trainingCompletedModulesJson; }
    public void setTrainingCompletedModulesJson(String trainingCompletedModulesJson) { this.trainingCompletedModulesJson = trainingCompletedModulesJson; }

    public int getResubmissionCount() { return resubmissionCount; }
    public void setResubmissionCount(int resubmissionCount) { this.resubmissionCount = resubmissionCount; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public List<OnboardingSagaActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<OnboardingSagaActivityLogEntity> activities) { this.activities = activities; }
}

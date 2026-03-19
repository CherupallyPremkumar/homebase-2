package com.homebase.ecom.compliance.port.out;

public interface NotificationPort {
    void notifyNewMandatoryAgreement(String agreementId, String agreementType);
    void notifyPolicyPublished(String policyId, String policyCategory);
}

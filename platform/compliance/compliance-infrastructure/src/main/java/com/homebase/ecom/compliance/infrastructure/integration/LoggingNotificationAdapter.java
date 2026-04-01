package com.homebase.ecom.compliance.infrastructure.integration;

import com.homebase.ecom.compliance.port.out.NotificationPort;
import java.util.logging.Logger;

public class LoggingNotificationAdapter implements NotificationPort {

    private static final Logger logger = Logger.getLogger(LoggingNotificationAdapter.class.getName());

    @Override
    public void notifyNewMandatoryAgreement(String agreementId, String agreementType) {
        logger.info("New mandatory agreement published: " + agreementId + " type=" + agreementType);
    }

    @Override
    public void notifyPolicyPublished(String policyId, String policyCategory) {
        logger.info("Platform policy published: " + policyId + " category=" + policyCategory);
    }
}

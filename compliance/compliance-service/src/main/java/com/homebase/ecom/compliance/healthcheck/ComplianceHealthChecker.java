package com.homebase.ecom.compliance.healthcheck;

import org.chenile.core.service.HealthChecker;
import org.chenile.core.service.HealthCheckInfo;

public class ComplianceHealthChecker implements HealthChecker {
    @Override
    public HealthCheckInfo healthCheck() {
        HealthCheckInfo info = new HealthCheckInfo();
        info.healthy = true;
        info.message = "Compliance service is healthy";
        return info;
    }
}

package com.homebase.ecom.review.service.healthcheck;

import org.chenile.core.service.HealthCheckInfo;
import org.chenile.core.service.HealthChecker;

public class ReviewHealthChecker implements HealthChecker {

	public static final String HEALTH_CHECK_MESSAGE = "Review is fine!";

	@Override
	public HealthCheckInfo healthCheck() {
		HealthCheckInfo healthCheckInfo = new HealthCheckInfo();
		healthCheckInfo.healthy = true;
		healthCheckInfo.statusCode = 0;
		healthCheckInfo.message = HEALTH_CHECK_MESSAGE;
		return healthCheckInfo;
	}

}

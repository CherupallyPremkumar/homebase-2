package com.homebase.ecom.supplierproduct.service.healthcheck;

import org.chenile.core.service.HealthCheckInfo;
import org.chenile.core.service.HealthChecker;

public class SupplierproductHealthChecker implements HealthChecker{

	public static final String HEALTH_CHECK_MESSAGE = "Supplierproduct is fine!";

	// Implement a health checker for the service.
	// Check all the dependent systems, DBs etc. 
	@Override
	public HealthCheckInfo healthCheck() {
		HealthCheckInfo healthCheckInfo = new HealthCheckInfo();
		healthCheckInfo.healthy = true;
		healthCheckInfo.statusCode = 0;
		healthCheckInfo.message = HEALTH_CHECK_MESSAGE;
		return healthCheckInfo;
	}

}

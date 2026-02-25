package com.ecommerce.payment.gateway;

/**
 * Provider for the currently active payment gateway type.
 *
 * This is used by scheduled jobs and admin workflows to decide which gateway's
 * reconciliation logic to run.
 */
public interface ActiveGatewayTypeProvider {
    String getActiveGatewayType();
}

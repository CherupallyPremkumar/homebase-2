package com.homebase.ecom.payment.gateway.service;

import org.chenile.workflow.api.StateEntityService;

/**
 * Service interface for Payment state entity operations.
 * Used by ProxyBuilder to create a client proxy.
 */
public interface PaymentService extends StateEntityService<Object> {
}

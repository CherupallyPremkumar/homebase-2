package com.homebase.ecom.payment.service;

import com.homebase.ecom.payment.domain.model.Payment;
import org.chenile.workflow.api.StateEntityService;

/**
 * Payment service interface extending Chenile StateEntityService.
 * All mutations go through STM processById.
 */
public interface PaymentService extends StateEntityService<Payment> {
}

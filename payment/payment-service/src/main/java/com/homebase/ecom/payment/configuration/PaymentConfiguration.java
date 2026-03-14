package com.homebase.ecom.payment.configuration;

import com.homebase.ecom.payment.repository.PaymentTransactionRepository;
import com.homebase.ecom.payment.service.impl.PaymentServiceImpl;
import com.homebase.ecom.payment.service.event.OrderItemEventConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to bridge the Payment module with the HomeBase monolithic
 * build.
 */
@Configuration
public class PaymentConfiguration {

    @Bean
    public OrderItemEventConsumer orderItemEventConsumer(
            PaymentServiceImpl paymentService,
            PaymentTransactionRepository paymentTransactionRepository) {
        return new OrderItemEventConsumer(paymentService, paymentTransactionRepository);
    }
}

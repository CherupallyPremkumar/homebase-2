package com.ecommerce.payment.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Factory to resolve the active PaymentGateway implementation based on
 * configuration.
 */
@Component
public class PaymentGatewayFactory {

    private static final Logger log = LoggerFactory.getLogger(PaymentGatewayFactory.class);

    private final List<PaymentGateway> gateways;

    @Value("${app.payment.gateway.active:stripe}")
    private String activeGatewayType;

    public PaymentGatewayFactory(List<PaymentGateway> gateways) {
        this.gateways = gateways;
    }

    /**
     * Returns the active gateway service based on the provided type.
     */
    public PaymentGateway getGatewayByType(String type) {
        return getGateway(type)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported or misconfigured payment gateway: " + type));
    }

    public Optional<PaymentGateway> getGateway(String type) {
        return gateways.stream()
                .filter(g -> g.getGatewayType().equalsIgnoreCase(type))
                .findFirst();
    }
}

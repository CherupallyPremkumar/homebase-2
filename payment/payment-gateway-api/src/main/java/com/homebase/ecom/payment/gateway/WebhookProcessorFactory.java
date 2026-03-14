package com.homebase.ecom.payment.gateway;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class WebhookProcessorFactory {

    private final List<WebhookProcessor> processors;

    public WebhookProcessorFactory(List<WebhookProcessor> processors) {
        this.processors = processors;
    }

    public Optional<WebhookProcessor> getProcessor(String gatewayType) {
        return processors.stream()
                .filter(p -> p.supports(gatewayType))
                .findFirst();
    }
}

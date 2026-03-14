package com.homebase.ecom.payment.client;

import org.chenile.base.response.GenericResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentClient {
    private final RestTemplate restTemplate;

    @Value("${payment.service.url:http://payment-service:8080}")
    private String baseUrl;

    public PaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GenericResponse<?> getPaymentById(String id) {
        return restTemplate.exchange(
            baseUrl + "/payment/" + id,
            HttpMethod.GET, new HttpEntity<>(headers()),
            new ParameterizedTypeReference<GenericResponse<?>>() {}
        ).getBody();
    }

    public GenericResponse<?> createPayment(Object request) {
        return restTemplate.exchange(
            baseUrl + "/payment",
            HttpMethod.POST, new HttpEntity<>(request, headers()),
            new ParameterizedTypeReference<GenericResponse<?>>() {}
        ).getBody();
    }

    public GenericResponse<?> processRefund(String paymentId, Object request) {
        return restTemplate.exchange(
            baseUrl + "/payment/" + paymentId + "/refund",
            HttpMethod.POST, new HttpEntity<>(request, headers()),
            new ParameterizedTypeReference<GenericResponse<?>>() {}
        ).getBody();
    }

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.set("x-chenile-tenant-id", "homebase");
        return h;
    }
}

package com.homebase.ecom.checkout.client;

import org.chenile.base.response.GenericResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CheckoutClient {
    private final RestTemplate restTemplate;

    @Value("${checkout.service.url:http://checkout-service:8080}")
    private String baseUrl;

    public CheckoutClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GenericResponse<?> initializeCheckout(Object request) {
        return restTemplate.exchange(
            baseUrl + "/api/v1/checkout/initialize",
            HttpMethod.POST, new HttpEntity<>(request, headers()),
            new ParameterizedTypeReference<GenericResponse<?>>() {}
        ).getBody();
    }

    public GenericResponse<?> initiatePayment(Object request) {
        return restTemplate.exchange(
            baseUrl + "/api/v1/checkout/payment/initiate",
            HttpMethod.POST, new HttpEntity<>(request, headers()),
            new ParameterizedTypeReference<GenericResponse<?>>() {}
        ).getBody();
    }

    public GenericResponse<?> authorizePayment(Object request) {
        return restTemplate.exchange(
            baseUrl + "/api/v1/checkout/payment/authorize",
            HttpMethod.POST, new HttpEntity<>(request, headers()),
            new ParameterizedTypeReference<GenericResponse<?>>() {}
        ).getBody();
    }

    public GenericResponse<?> getOrderDetails(String orderId) {
        return restTemplate.exchange(
            baseUrl + "/api/v1/checkout/order/" + orderId,
            HttpMethod.GET, new HttpEntity<>(headers()),
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

package com.homebase.ecom.catalog.client;

import org.chenile.base.response.GenericResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CatalogClient {
    private final RestTemplate restTemplate;

    @Value("${catalog.service.url:http://catalog-service:8080}")
    private String baseUrl;

    public CatalogClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GenericResponse<?> getCategories() {
        return restTemplate.exchange(
            baseUrl + "/api/categories",
            HttpMethod.GET, new HttpEntity<>(headers()),
            new ParameterizedTypeReference<GenericResponse<?>>() {}
        ).getBody();
    }

    public GenericResponse<?> getCategoryById(String id) {
        return restTemplate.exchange(
            baseUrl + "/api/categories/" + id,
            HttpMethod.GET, new HttpEntity<>(headers()),
            new ParameterizedTypeReference<GenericResponse<?>>() {}
        ).getBody();
    }

    public GenericResponse<?> getCollections() {
        return restTemplate.exchange(
            baseUrl + "/api/collections",
            HttpMethod.GET, new HttpEntity<>(headers()),
            new ParameterizedTypeReference<GenericResponse<?>>() {}
        ).getBody();
    }

    public GenericResponse<?> getCollectionById(String id) {
        return restTemplate.exchange(
            baseUrl + "/api/collections/" + id,
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

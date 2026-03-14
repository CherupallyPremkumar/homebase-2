package com.homebase.ecom.cart.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import com.homebase.ecom.cart.exception.*;
import org.chenile.base.exception.BadRequestException;
import org.chenile.base.exception.ErrorNumException;
import org.chenile.base.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Advanced error decoder for the Cart client.
 * Maps HTTP error responses to specific domain exceptions defined in cart-api.
 */
public class CartErrorDecoder implements ErrorDecoder {
    
    @Override
    public Exception decode(String methodKey, Response response) {
        String body = extractBody(response);

        return switch (response.status()) {
            case 400 -> mapBadRequest(body);
            case 404 -> new NotFoundException("Cart or requested resource not found");
            case 401, 403 -> new ErrorNumException(response.status(), "Security violation when calling Cart service");
            default -> new ErrorNumException(response.status(), "Cart service returned an error: " + response.reason());
        };
    }

    private String extractBody(Response response) {
        if (response.body() == null) return "";
        try (InputStream inputStream = response.body().asInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    private Exception mapBadRequest(String body) {
        if (body.contains("Multi-seller")) {
            return new MultiSellerViolationException(body);
        } else if (body.contains("limit exceeded") && body.contains("unique items")) {
            return new CartLimitExceededException(body);
        } else if (body.contains("Currency mismatch")) {
            return new CurrencyMismatchException(body);
        } else if (body.contains("Quantity limit exceeded")) {
            return new QuantityLimitExceededException(body);
        }
        return new BadRequestException("Invalid request sent to Cart Service: " + body);
    }
}

package com.homebase.ecom.checkout.infrastructure.client;

import com.homebase.ecom.shared.Money;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "shipping-service", url = "${app.services.shipping-url}")
public interface ShippingServiceClient {

    @PostMapping("/api/shipping/validate")
    ShippingValidationResponse validateAddress(@RequestBody ShippingAddress address);

    @PostMapping("/api/shipping/estimate")
    ShippingEstimateResponse getEstimates(@RequestBody ShippingEstimateRequest request);

    class ShippingAddress {
        private String firstName;
        private String lastName;
        private String addressLine1;
        private String city;
        private String state;
        private String zipCode;
        private String country;
        // Getters/Setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
    }

    class ShippingValidationResponse {
        private boolean valid;
        private String message;
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    class ShippingEstimateRequest {
        private UUID cartId;
        private ShippingAddress destination;
        public UUID getCartId() { return cartId; }
        public void setCartId(UUID cartId) { this.cartId = cartId; }
        public ShippingAddress getDestination() { return destination; }
        public void setDestination(ShippingAddress destination) { this.destination = destination; }
    }

    class ShippingEstimateResponse {
        private List<ShippingOption> options;
        public List<ShippingOption> getOptions() { return options; }
        public void setOptions(List<ShippingOption> options) { this.options = options; }
    }

    class ShippingOption {
        private String method;
        private Money cost;
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public Money getCost() { return cost; }
        public void setCost(Money cost) { this.cost = cost; }
    }
}

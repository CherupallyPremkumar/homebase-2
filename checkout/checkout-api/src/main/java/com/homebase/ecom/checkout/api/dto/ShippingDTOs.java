package com.homebase.ecom.checkout.api.dto;

import java.util.List;
import java.util.UUID;

/**
 * Container for Shipping related DTOs
 */
public class ShippingDTOs {

    public static class ShippingEstimateRequest {
        private UUID cartId;
        private AddressDTO destination;

        public UUID getCartId() {
            return cartId;
        }

        public void setCartId(UUID cartId) {
            this.cartId = cartId;
        }

        public AddressDTO getDestination() {
            return destination;
        }

        public void setDestination(AddressDTO destination) {
            this.destination = destination;
        }
    }

    public static class ShippingEstimateResponse {
        private String estimateId;
        private List<ShippingOptionDTO> options;

        public String getEstimateId() {
            return estimateId;
        }

        public void setEstimateId(String estimateId) {
            this.estimateId = estimateId;
        }

        public List<ShippingOptionDTO> getOptions() {
            return options;
        }

        public void setOptions(List<ShippingOptionDTO> options) {
            this.options = options;
        }
    }
}

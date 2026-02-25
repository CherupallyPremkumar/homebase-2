package com.ecommerce.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutSessionResponse {
    private String sessionId;
    private String clientSecret;
    private String checkoutUrl;
    private String gatewayType;
}

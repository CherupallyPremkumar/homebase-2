package com.ecommerce.payment.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PaymentGatewayFactoryTest {

    @Mock
    private PaymentGateway stripeGateway;

    @Mock
    private PaymentGateway razorpayGateway;

    private PaymentGatewayFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(stripeGateway.getGatewayType()).thenReturn("stripe");
        when(razorpayGateway.getGatewayType()).thenReturn("razorpay");

        List<PaymentGateway> gateways = Arrays.asList(stripeGateway, razorpayGateway);
        factory = new PaymentGatewayFactory(gateways);
    }

    @Test
    void testGetStripeGateway() {
        Optional<PaymentGateway> gateway = factory.getGateway("stripe");
        assertTrue(gateway.isPresent());
        assertEquals(stripeGateway, gateway.get());
    }

    @Test
    void testGetRazorpayGateway() {
        Optional<PaymentGateway> gateway = factory.getGateway("razorpay");
        assertTrue(gateway.isPresent());
        assertEquals(razorpayGateway, gateway.get());
    }

    @Test
    void testGetGatewayReturnsEmptyForUnsupportedGateway() {
        Optional<PaymentGateway> gateway = factory.getGateway("unsupported");
        assertFalse(gateway.isPresent());
    }
}

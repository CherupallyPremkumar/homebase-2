package com.homebase.ecom.checkout.infrastructure.client.adapter;

import com.homebase.ecom.checkout.api.dto.AddressDTO;
import com.homebase.ecom.checkout.infrastructure.client.ShippingServiceClient;
import com.homebase.ecom.checkout.service.port.ShippingClient;
import org.springframework.stereotype.Component;

@Component
public class ShippingClientAdapter implements ShippingClient {
    private final ShippingServiceClient shippingServiceClient;

    public ShippingClientAdapter(ShippingServiceClient shippingServiceClient) {
        this.shippingServiceClient = shippingServiceClient;
    }

    @Override
    public boolean validateAddress(AddressDTO address) {
        // Map AddressDTO to backend-specific ShippingAddress if necessary
        // In this WIP, we'll assume valid if the call returns success
        ShippingServiceClient.ShippingAddress backendAddress = new ShippingServiceClient.ShippingAddress();
        if (address != null) {
            backendAddress.setAddressLine1(address.getAddressLine1());
            backendAddress.setCity(address.getCity());
            backendAddress.setZipCode(address.getZipCode());
            backendAddress.setCountry(address.getCountry());
        }
        return shippingServiceClient.validateAddress(backendAddress).isValid();
    }
}

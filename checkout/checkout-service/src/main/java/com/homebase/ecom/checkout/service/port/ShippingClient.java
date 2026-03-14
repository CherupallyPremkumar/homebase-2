package com.homebase.ecom.checkout.service.port;

import com.homebase.ecom.checkout.api.dto.AddressDTO;

public interface ShippingClient {
    boolean validateAddress(AddressDTO address);
}

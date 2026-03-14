package com.homebase.ecom.checkout.domain.service;

import com.homebase.ecom.checkout.domain.model.CartSnapshot;
import java.util.UUID;

public interface ICartService {
    CartSnapshot lockCart(UUID cartId);
    void unlockCart(UUID cartId);
}

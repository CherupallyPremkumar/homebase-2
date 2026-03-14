package com.homebase.ecom.cart.consumer.service;

import com.homebase.ecom.cart.repository.CartItemRepository;
import com.homebase.ecom.cart.model.CartItemStatus;
import com.homebase.ecom.shared.event.ProductPriceChangedEvent;
import com.homebase.ecom.shared.event.ProductStockUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for performing batch updates on cart items based on external events.
 */
@Service
public class CartBatchUpdateService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public void processPriceChange(ProductPriceChangedEvent event) {
        cartItemRepository.updatePriceAndSeller(
                event.getProductId(),
                event.getNewPrice().getAmount(),
                event.getNewPrice().getCurrency(),
                event.getSellerId(),
                CartItemStatus.PRICE_CHANGED);
    }

    @Transactional
    public void processStockUpdate(ProductStockUpdatedEvent event) {
        if (event.getStatus() == ProductStockUpdatedEvent.StockStatus.OUT_OF_STOCK
                || event.getAvailableQuantity() <= 0) {
            cartItemRepository.updateStatusByProductId(event.getProductId(), CartItemStatus.OUT_OF_STOCK);
        } else {
            // If it was out of stock, mark it back to available?
            // Usually simpler to just mark available and let the next sync check validate
            // everything.
            cartItemRepository.updateStatusByProductId(event.getProductId(), CartItemStatus.AVAILABLE);
        }
    }
}

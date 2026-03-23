package com.homebase.ecom.cart.service.impl;

import com.homebase.ecom.cart.dto.CartDto;
import com.homebase.ecom.cart.dto.CreateCartPayload;
import com.homebase.ecom.cart.service.mapper.CartDtoMapper;
import com.homebase.ecom.cart.service.handler.CartExternalEventHandler;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.service.CartService;
import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;

public class CartServiceImpl extends HmStateEntityServiceImpl<Cart> implements CartService {

    private final CartDtoMapper cartDtoMapper;
    private final CartExternalEventHandler externalEventHandler;

    public CartServiceImpl(STM<Cart> stm, STMActionsInfoProvider stmActionsInfoProvider,
                           EntityStore<Cart> entityStore, CartDtoMapper cartDtoMapper,
                           CartExternalEventHandler externalEventHandler) {
        super(stm, stmActionsInfoProvider, entityStore);
        this.cartDtoMapper = cartDtoMapper;
        this.externalEventHandler = externalEventHandler;
    }

    @Override
    public CartDto createCart(CreateCartPayload payload) {
        Cart cart = new Cart();
        cart.setCustomerId(payload.customerId);
        cart.setSessionId(payload.sessionId);
        cart.description = payload.getComment();
        StateEntityServiceResponse<Cart> response = create(cart);
        return cartDtoMapper.toDto(response.getMutatedEntity());
    }

    @Override
    public CartDto getCart(String cartId) {
        StateEntityServiceResponse<Cart> response = retrieve(cartId);
        return cartDtoMapper.toDto(response.getMutatedEntity());
    }

    @Override
    public CartDto getActiveCartForCustomer(String customerId) {
        // TODO: query entity store for active cart by customerId
        return null;
    }

    @Override
    public void onExternalEvent(String eventPayload) {
        externalEventHandler.handle(eventPayload, this);
    }

    @Override
    public CartDto proceedById(String cartId, String eventId, Object payload) {
        StateEntityServiceResponse<Cart> response = super.processById(cartId, eventId, payload);
        return cartDtoMapper.toDto(response.getMutatedEntity());
    }

    @Override
    public StateEntityServiceResponse<Cart> create(Cart entity) {
        return super.create(entity);
    }
}

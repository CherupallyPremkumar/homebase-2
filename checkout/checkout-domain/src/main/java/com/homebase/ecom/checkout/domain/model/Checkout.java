package com.homebase.ecom.checkout.domain.model;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Checkout Aggregate Root
 */
public class Checkout {
    
    private UUID checkoutId;
    private final UUID cartId;
    private final UUID userId;
    private UUID orderId;

    private CheckoutState state;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    
    // Snapshots
    private CartSnapshot lockedCart;
    private PriceSnapshot lockedPrice;
    private OrderDetails orderDetails;
    private PaymentDetails paymentDetails;
    
    private final String idempotencyKey;
    private final List<CheckoutAuditEntry> auditLog;
    
    private static final Map<CheckoutState, Set<CheckoutState>> VALID_TRANSITIONS;
    
    static {
        VALID_TRANSITIONS = new EnumMap<>(CheckoutState.class);
        VALID_TRANSITIONS.put(CheckoutState.INITIALIZED, EnumSet.of(CheckoutState.CART_LOCKED, CheckoutState.ABANDONED, CheckoutState.CANCELLED));
        VALID_TRANSITIONS.put(CheckoutState.CART_LOCKED, EnumSet.of(CheckoutState.PRICE_LOCKED, CheckoutState.ABANDONED, CheckoutState.CANCELLED));
        VALID_TRANSITIONS.put(CheckoutState.PRICE_LOCKED, EnumSet.of(CheckoutState.ORDER_CREATED, CheckoutState.ABANDONED, CheckoutState.CANCELLED));
        VALID_TRANSITIONS.put(CheckoutState.ORDER_CREATED, EnumSet.of(CheckoutState.PAYMENT_INITIATED, CheckoutState.ABANDONED, CheckoutState.CANCELLED));
        VALID_TRANSITIONS.put(CheckoutState.PAYMENT_INITIATED, EnumSet.of(CheckoutState.PAYMENT_PENDING, CheckoutState.ABANDONED, CheckoutState.CANCELLED));
        VALID_TRANSITIONS.put(CheckoutState.PAYMENT_PENDING, EnumSet.of(CheckoutState.PAYMENT_SUCCESS, CheckoutState.PAYMENT_FAILED));
        VALID_TRANSITIONS.put(CheckoutState.PAYMENT_SUCCESS, EnumSet.of(CheckoutState.COMPLETED));
        VALID_TRANSITIONS.put(CheckoutState.PAYMENT_FAILED, EnumSet.of(CheckoutState.ABANDONED));
        VALID_TRANSITIONS.put(CheckoutState.COMPLETED, EnumSet.noneOf(CheckoutState.class));
        VALID_TRANSITIONS.put(CheckoutState.ABANDONED, EnumSet.noneOf(CheckoutState.class));
        VALID_TRANSITIONS.put(CheckoutState.CANCELLED, EnumSet.noneOf(CheckoutState.class));
    }
    
    public Checkout(UUID cartId, UUID userId, String idempotencyKey) {
        this.checkoutId = UUID.randomUUID();
        this.cartId = cartId;
        this.userId = userId;
        this.idempotencyKey = idempotencyKey;
        this.state = CheckoutState.INITIALIZED;
        this.createdAt = LocalDateTime.now();
        this.auditLog = new ArrayList<>();
        addAuditEntry("CHECKOUT_INITIALIZED", null, CheckoutState.INITIALIZED);
    }
    
    public void transitionTo(CheckoutState newState) {
        Set<CheckoutState> allowed = VALID_TRANSITIONS.get(this.state);
        if (allowed == null || !allowed.contains(newState)) {
            // Simple string based exception since I didn't merge the specific one yet
            throw new RuntimeException("Invalid transition from " + this.state + " to " + newState);
        }
        CheckoutState oldState = this.state;
        this.state = newState;
        addAuditEntry("STATE_CHANGED", oldState, newState);
        
        if (newState == CheckoutState.CART_LOCKED) this.startedAt = LocalDateTime.now();
        if (newState == CheckoutState.COMPLETED) this.completedAt = LocalDateTime.now();
        if (newState == CheckoutState.ABANDONED) this.cancelledAt = LocalDateTime.now();
    }
    
    private void addAuditEntry(String action, CheckoutState from, CheckoutState to) {
        auditLog.add(new CheckoutAuditEntry(action, from, to, LocalDateTime.now()));
    }
    
    // Snapshots
    public void setLockedCart(CartSnapshot cart) { this.lockedCart = cart; }
    public void setLockedPrice(PriceSnapshot price) { this.lockedPrice = price; }
    public void setOrderDetails(OrderDetails order) { this.orderDetails = order; this.orderId = order.getOrderId(); }
    public void setPaymentDetails(PaymentDetails payment) { this.paymentDetails = payment; }
    
    // Getters
    public UUID getCheckoutId() { return checkoutId; }
    public void setCheckoutId(UUID checkoutId) { this.checkoutId = checkoutId; }
    public UUID getCartId() { return cartId; }
    public UUID getUserId() { return userId; }
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public CheckoutState getState() { return state; }
    public void setState(CheckoutState state) { this.state = state; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public CartSnapshot getLockedCart() { return lockedCart; }
    public PriceSnapshot getLockedPrice() { return lockedPrice; }
    public OrderDetails getOrderDetails() { return orderDetails; }
    public PaymentDetails getPaymentDetails() { return paymentDetails; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public List<CheckoutAuditEntry> getAuditLog() { return Collections.unmodifiableList(auditLog); }
}

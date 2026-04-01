package com.homebase.ecom.cart.infrastructure.persistence.mapper;

import com.homebase.ecom.cart.infrastructure.persistence.entity.CartEntity;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartItemEntity;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.shared.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for CartMapper bidirectional mapping.
 * All money values are in paise (smallest currency unit).
 */
class CartMapperTest {

    private CartMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CartMapper();
    }

    // ═══════════════════════════════════════════════════════════════════
    // Model -> Entity
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void toEntity_nullModel_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_mapsAllFields() {
        Cart model = createSampleCart();

        CartEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("cart-123", entity.getId());
        assertEquals("cust-456", entity.getCustomerId());
        assertEquals("session-789", entity.getSessionId());
        assertEquals(450000L, entity.getSubtotal());
        assertEquals("INR", entity.getCurrency());
        assertEquals("WELCOME10,SUMMER20", entity.getCouponCodes());
        assertEquals(10000L, entity.getDiscountAmount());
        assertNotNull(entity.getExpiresAt());
        assertEquals("Test cart", entity.description);
    }

    @Test
    void toEntity_mapsItems() {
        Cart model = createSampleCart();

        CartEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getItems());
        assertEquals(2, entity.getItems().size());

        CartItemEntity item1 = entity.getItems().get(0);
        assertEquals("prod-001", item1.getProductId());
        assertEquals("var-001", item1.getVariantId());
        assertEquals("SKU-001", item1.getSku());
        assertEquals("Silk Scarf", item1.getProductName());
        assertEquals(2, item1.getQuantity());
        assertEquals(150000L, item1.getUnitPrice());
    }

    @Test
    void toEntity_emptyCouponCodes_setsNull() {
        Cart model = new Cart();
        model.setId("cart-empty");
        model.setCouponCodes(new ArrayList<>());

        CartEntity entity = mapper.toEntity(model);
        assertNull(entity.getCouponCodes());
    }

    // ═══════════════════════════════════════════════════════════════════
    // Entity -> Model
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void toModel_nullEntity_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toModel_mapsAllFields() {
        CartEntity entity = createSampleCartEntity();

        Cart model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("cart-123", model.getId());
        assertEquals("cust-456", model.getCustomerId());
        assertEquals("session-789", model.getSessionId());
        assertEquals(Money.of(450000L, "INR"), model.getSubtotal());
        assertEquals("INR", model.getCurrency());
        assertEquals(2, model.getCouponCodes().size());
        assertTrue(model.getCouponCodes().contains("WELCOME10"));
        assertTrue(model.getCouponCodes().contains("SUMMER20"));
        assertEquals(Money.of(10000L, "INR"), model.getDiscountAmount());
        assertNotNull(model.getExpiresAt());
        assertEquals("Test cart", model.description);
    }

    @Test
    void toModel_mapsItems() {
        CartEntity entity = createSampleCartEntity();

        Cart model = mapper.toModel(entity);

        assertNotNull(model.getItems());
        assertEquals(1, model.getItems().size());

        CartItem item = model.getItems().get(0);
        assertEquals("prod-001", item.getProductId());
        assertEquals("var-001", item.getVariantId());
        assertEquals("SKU-001", item.getSku());
        assertEquals("Silk Scarf", item.getProductName());
        assertEquals(2, item.getQuantity());
        assertEquals(Money.of(150000L, "INR"), item.getUnitPrice());
    }

    @Test
    void toModel_nullCouponCodes_returnsEmptyList() {
        CartEntity entity = new CartEntity();
        entity.setId("cart-null");
        entity.setCouponCodes(null);

        Cart model = mapper.toModel(entity);
        assertNotNull(model.getCouponCodes());
        assertTrue(model.getCouponCodes().isEmpty());
    }

    @Test
    void toModel_emptyCouponCodes_returnsEmptyList() {
        CartEntity entity = new CartEntity();
        entity.setId("cart-empty");
        entity.setCouponCodes("");

        Cart model = mapper.toModel(entity);
        assertNotNull(model.getCouponCodes());
        assertTrue(model.getCouponCodes().isEmpty());
    }

    // ═══════════════════════════════════════════════════════════════════
    // Roundtrip
    // ═══════════════════════════════════════════════════════════════════

    @Test
    void roundtrip_modelToEntityToModel_preservesData() {
        Cart original = createSampleCart();

        CartEntity entity = mapper.toEntity(original);
        Cart roundtripped = mapper.toModel(entity);

        assertEquals(original.getId(), roundtripped.getId());
        assertEquals(original.getCustomerId(), roundtripped.getCustomerId());
        assertEquals(original.getSessionId(), roundtripped.getSessionId());
        assertEquals(original.getSubtotal(), roundtripped.getSubtotal());
        assertEquals(original.getCurrency(), roundtripped.getCurrency());
        assertEquals(original.getCouponCodes(), roundtripped.getCouponCodes());
        assertEquals(original.getDiscountAmount(), roundtripped.getDiscountAmount());
        assertEquals(original.getItems().size(), roundtripped.getItems().size());
    }

    // ═══════════════════════════════════════════════════════════════════
    // Helpers
    // ═══════════════════════════════════════════════════════════════════

    private Cart createSampleCart() {
        Cart cart = new Cart();
        cart.setId("cart-123");
        cart.setCustomerId("cust-456");
        cart.setSessionId("session-789");
        cart.setSubtotal(Money.of(450000L, "INR"));      // Rs.4500.00
        cart.setCouponCodes(new ArrayList<>(Arrays.asList("WELCOME10", "SUMMER20")));
        cart.setDiscountAmount(Money.of(10000L, "INR"));  // Rs.100.00
        cart.setExpiresAt(LocalDateTime.now().plusHours(72));
        cart.description = "Test cart";

        CartItem item1 = new CartItem();
        item1.setId("item-001");
        item1.setProductId("prod-001");
        item1.setVariantId("var-001");
        item1.setSku("SKU-001");
        item1.setProductName("Silk Scarf");
        item1.setQuantity(2);
        item1.setUnitPrice(Money.of(150000L, "INR"));    // Rs.1500.00

        CartItem item2 = new CartItem();
        item2.setId("item-002");
        item2.setProductId("prod-002");
        item2.setVariantId("var-002");
        item2.setSku("SKU-002");
        item2.setProductName("Tote Bag");
        item2.setQuantity(1);
        item2.setUnitPrice(Money.of(80000L, "INR"));     // Rs.800.00

        cart.setItems(new ArrayList<>(List.of(item1, item2)));
        return cart;
    }

    private CartEntity createSampleCartEntity() {
        CartEntity entity = new CartEntity();
        entity.setId("cart-123");
        entity.setCustomerId("cust-456");
        entity.setSessionId("session-789");
        entity.setSubtotal(450000L);        // Rs.4500.00 in paise
        entity.setCurrency("INR");
        entity.setCouponCodes("WELCOME10,SUMMER20");
        entity.setDiscountAmount(10000L);   // Rs.100.00 in paise
        entity.setExpiresAt(LocalDateTime.now().plusHours(72));
        entity.description = "Test cart";

        CartItemEntity item = new CartItemEntity();
        item.setId("item-001");
        item.setCart(entity);
        item.setProductId("prod-001");
        item.setVariantId("var-001");
        item.setSku("SKU-001");
        item.setProductName("Silk Scarf");
        item.setQuantity(2);
        item.setUnitPrice(150000L);         // Rs.1500.00 in paise

        entity.setItems(new ArrayList<>(List.of(item)));
        return entity;
    }
}

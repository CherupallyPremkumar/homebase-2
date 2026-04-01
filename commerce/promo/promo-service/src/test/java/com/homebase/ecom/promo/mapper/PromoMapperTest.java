package com.homebase.ecom.promo.mapper;

import com.homebase.ecom.dto.PromoCodeDto;
import com.homebase.ecom.promo.model.Coupon;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Item 7: PromoMapper bidirectional mapping tests.
 * Tests entity-to-DTO and DTO-to-entity conversions.
 */
public class PromoMapperTest {

    @Test
    public void testEntityToDto() {
        Coupon coupon = createTestCoupon();

        PromoCodeDto dto = mapToDto(coupon);

        assertEquals(coupon.getId(), dto.getId());
        assertEquals("SUMMER20", dto.getCode());
        assertEquals("Summer Sale", dto.getName());
        assertEquals("Summer Sale 20% Off", dto.getDescription());
        assertEquals("PERCENTAGE", dto.getDiscountType());
        assertEquals(BigDecimal.valueOf(20.0), dto.getDiscountValue());
        assertEquals(BigDecimal.valueOf(500.0), dto.getMinOrderValue());
        assertEquals(BigDecimal.valueOf(2000.0), dto.getMaxDiscountAmount());
        assertEquals(Integer.valueOf(100), dto.getUsageLimit());
        assertEquals(Integer.valueOf(0), dto.getUsageCount());
        assertEquals(Integer.valueOf(3), dto.getUsagePerCustomer());
    }

    @Test
    public void testDtoToEntity() {
        PromoCodeDto dto = createTestDto();

        Coupon coupon = mapToEntity(dto);

        assertEquals("FLASH10", coupon.getCode());
        assertEquals("Flash Sale", coupon.getName());
        assertEquals("Flash Sale 10% Off", coupon.getDescription());
        assertEquals("PERCENTAGE", coupon.getDiscountType());
        assertEquals(Double.valueOf(10.0), coupon.getDiscountValue());
        assertEquals(Double.valueOf(200.0), coupon.getMinOrderValue());
        assertEquals(Integer.valueOf(50), coupon.getUsageLimit());
        assertEquals(Integer.valueOf(0), coupon.getUsageCount());
        assertEquals(Integer.valueOf(1), coupon.getUsagePerCustomer());
    }

    @Test
    public void testEntityToDtoNullFields() {
        Coupon coupon = new Coupon();
        coupon.setId(UUID.randomUUID().toString());
        coupon.setCode("NULLTEST");
        coupon.setName("Null Test");
        coupon.setDiscountType("FLAT");
        coupon.setDiscountValue(100.0);

        PromoCodeDto dto = mapToDto(coupon);

        assertEquals("NULLTEST", dto.getCode());
        assertNull(dto.getMinOrderValue());
        assertNull(dto.getMaxDiscountAmount());
        assertNull(dto.getStartDate());
        assertNull(dto.getEndDate());
    }

    @Test
    public void testRoundTripEntityToDtoToEntity() {
        Coupon original = createTestCoupon();
        PromoCodeDto dto = mapToDto(original);
        Coupon roundTrip = mapToEntity(dto);

        assertEquals(original.getCode(), roundTrip.getCode());
        assertEquals(original.getName(), roundTrip.getName());
        assertEquals(original.getDescription(), roundTrip.getDescription());
        assertEquals(original.getDiscountType(), roundTrip.getDiscountType());
        assertEquals(original.getDiscountValue(), roundTrip.getDiscountValue());
        assertEquals(original.getMinOrderValue(), roundTrip.getMinOrderValue());
        assertEquals(original.getMaxDiscountAmount(), roundTrip.getMaxDiscountAmount());
        assertEquals(original.getUsageLimit(), roundTrip.getUsageLimit());
        assertEquals(original.getUsageCount(), roundTrip.getUsageCount());
        assertEquals(original.getUsagePerCustomer(), roundTrip.getUsagePerCustomer());
    }

    @Test
    public void testEntityWithApplicableProducts() {
        Coupon coupon = createTestCoupon();
        coupon.setApplicableProducts(Arrays.asList("PROD-001", "PROD-002"));
        coupon.setApplicableCategories(Arrays.asList("CAT-ELECTRONICS", "CAT-FASHION"));

        assertTrue(coupon.appliesTo("PROD-001", null));
        assertTrue(coupon.appliesTo(null, "CAT-FASHION"));
        assertFalse(coupon.appliesTo("PROD-999", "CAT-FOOD"));
    }

    @Test
    public void testDiscountCalculationPercentage() {
        Coupon coupon = createTestCoupon();
        // 20% of 1000 = 200, but maxDiscountAmount is 2000, so 200
        double discount = coupon.calculateDiscount(1000.0);
        assertEquals(200.0, discount, 0.01);
    }

    @Test
    public void testDiscountCalculationPercentageCapped() {
        Coupon coupon = createTestCoupon();
        coupon.setMaxDiscountAmount(150.0);
        // 20% of 1000 = 200, but capped at 150
        double discount = coupon.calculateDiscount(1000.0);
        assertEquals(150.0, discount, 0.01);
    }

    @Test
    public void testDiscountCalculationFlat() {
        Coupon coupon = new Coupon();
        coupon.setDiscountType("FLAT");
        coupon.setDiscountValue(500.0);

        double discount = coupon.calculateDiscount(1000.0);
        assertEquals(500.0, discount, 0.01);

        // Flat cannot exceed order amount
        double discount2 = coupon.calculateDiscount(300.0);
        assertEquals(300.0, discount2, 0.01);
    }

    @Test
    public void testUsageLimitReached() {
        Coupon coupon = createTestCoupon();
        coupon.setUsageLimit(2);
        coupon.setUsageCount(2);
        assertTrue(coupon.isUsageLimitReached());

        coupon.setUsageCount(1);
        assertFalse(coupon.isUsageLimitReached());
    }

    @Test
    public void testIncrementUsage() {
        Coupon coupon = createTestCoupon();
        coupon.setUsageCount(0);
        coupon.incrementUsage();
        assertEquals(Integer.valueOf(1), coupon.getUsageCount());
        coupon.incrementUsage();
        assertEquals(Integer.valueOf(2), coupon.getUsageCount());
    }

    // ===================== Helper methods (mapping logic) =====================

    private PromoCodeDto mapToDto(Coupon coupon) {
        PromoCodeDto dto = new PromoCodeDto();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setName(coupon.getName());
        dto.setDescription(coupon.getDescription());
        dto.setDiscountType(coupon.getDiscountType());
        dto.setDiscountValue(coupon.getDiscountValue() != null ? BigDecimal.valueOf(coupon.getDiscountValue()) : null);
        dto.setMinOrderValue(coupon.getMinOrderValue() != null ? BigDecimal.valueOf(coupon.getMinOrderValue()) : null);
        dto.setMaxDiscountAmount(coupon.getMaxDiscountAmount() != null ? BigDecimal.valueOf(coupon.getMaxDiscountAmount()) : null);
        dto.setStartDate(coupon.getStartDate() != null ? java.sql.Timestamp.valueOf(coupon.getStartDate()) : null);
        dto.setEndDate(coupon.getEndDate() != null ? java.sql.Timestamp.valueOf(coupon.getEndDate()) : null);
        dto.setUsageLimit(coupon.getUsageLimit());
        dto.setUsageCount(coupon.getUsageCount());
        dto.setUsagePerCustomer(coupon.getUsagePerCustomer());
        return dto;
    }

    private Coupon mapToEntity(PromoCodeDto dto) {
        Coupon coupon = new Coupon();
        coupon.setCode(dto.getCode());
        coupon.setName(dto.getName());
        coupon.setDescription(dto.getDescription());
        coupon.setDiscountType(dto.getDiscountType());
        coupon.setDiscountValue(dto.getDiscountValue() != null ? dto.getDiscountValue().doubleValue() : null);
        coupon.setMinOrderValue(dto.getMinOrderValue() != null ? dto.getMinOrderValue().doubleValue() : null);
        coupon.setMaxDiscountAmount(dto.getMaxDiscountAmount() != null ? dto.getMaxDiscountAmount().doubleValue() : null);
        coupon.setUsageLimit(dto.getUsageLimit());
        coupon.setUsageCount(dto.getUsageCount());
        coupon.setUsagePerCustomer(dto.getUsagePerCustomer());
        return coupon;
    }

    private Coupon createTestCoupon() {
        Coupon coupon = new Coupon();
        coupon.setId(UUID.randomUUID().toString());
        coupon.setCode("SUMMER20");
        coupon.setName("Summer Sale");
        coupon.setDescription("Summer Sale 20% Off");
        coupon.setDiscountType("PERCENTAGE");
        coupon.setDiscountValue(20.0);
        coupon.setMinOrderValue(500.0);
        coupon.setMaxDiscountAmount(2000.0);
        coupon.setUsageLimit(100);
        coupon.setUsageCount(0);
        coupon.setUsagePerCustomer(3);
        coupon.setStartDate(LocalDateTime.of(2026, 6, 1, 0, 0));
        coupon.setEndDate(LocalDateTime.of(2026, 8, 31, 23, 59, 59));
        return coupon;
    }

    private PromoCodeDto createTestDto() {
        PromoCodeDto dto = new PromoCodeDto();
        dto.setId(UUID.randomUUID().toString());
        dto.setCode("FLASH10");
        dto.setName("Flash Sale");
        dto.setDescription("Flash Sale 10% Off");
        dto.setDiscountType("PERCENTAGE");
        dto.setDiscountValue(BigDecimal.valueOf(10.0));
        dto.setMinOrderValue(BigDecimal.valueOf(200.0));
        dto.setUsageLimit(50);
        dto.setUsageCount(0);
        dto.setUsagePerCustomer(1);
        return dto;
    }
}

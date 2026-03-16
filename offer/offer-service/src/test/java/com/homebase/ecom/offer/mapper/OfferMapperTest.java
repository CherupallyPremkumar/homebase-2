package com.homebase.ecom.offer.mapper;

import com.homebase.ecom.offer.api.dto.OfferDTO;
import com.homebase.ecom.offer.domain.model.Offer;
import com.homebase.ecom.offer.domain.model.OfferType;
import com.homebase.ecom.offer.infrastructure.persistence.entity.OfferEntity;
import com.homebase.ecom.offer.infrastructure.persistence.mapper.OfferMapper;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Item 7: Bidirectional mapper unit tests for OfferMapper.
 */
public class OfferMapperTest {

    private final OfferMapper mapper = new OfferMapper();

    // ── DTO → Domain Model ─────────────────────────────────────────────────

    @Test
    public void testDtoToModel() {
        OfferDTO dto = createSampleDTO();
        Offer model = mapper.toModel(dto);

        assertEquals("offer-123", model.getId());
        assertEquals("product-001", model.getProductId());
        assertEquals("seller-001", model.getSupplierId());
        assertEquals(OfferType.DEAL, model.getOfferType());
        assertEquals("Summer Sale", model.getTitle());
        assertEquals("Big discount on mugs", model.getDescription());
        assertEquals(0, new BigDecimal("100.00").compareTo(model.getOriginalPrice()));
        assertEquals(0, new BigDecimal("80.00").compareTo(model.getOfferPrice()));
        assertEquals(0, new BigDecimal("20.00").compareTo(model.getDiscountPercent()));
        assertEquals(500, model.getMaxQuantity());
        assertEquals(10, model.getSoldQuantity());
        assertEquals(0, new BigDecimal("4.50").compareTo(model.getSellerRating()));
    }

    @Test
    public void testDtoToModel_nullInput() {
        assertNull(mapper.toModel((OfferDTO) null));
    }

    // ── Domain Model → DTO ─────────────────────────────────────────────────

    @Test
    public void testModelToDto() {
        Offer model = createSampleModel();
        OfferDTO dto = mapper.toDTO(model);

        assertEquals("offer-123", dto.getId());
        assertEquals("product-001", dto.getProductId());
        assertEquals("seller-001", dto.getSupplierId());
        assertEquals("DEAL", dto.getOfferType());
        assertEquals("Summer Sale", dto.getTitle());
        assertEquals("Big discount on mugs", dto.getDescription());
        assertEquals(0, new BigDecimal("100.00").compareTo(dto.getOriginalPrice()));
        assertEquals(0, new BigDecimal("80.00").compareTo(dto.getOfferPrice()));
        assertEquals(0, new BigDecimal("20.00").compareTo(dto.getDiscountPercent()));
        assertEquals(500, dto.getMaxQuantity());
        assertEquals(10, dto.getSoldQuantity());
        assertEquals(0, new BigDecimal("4.50").compareTo(dto.getSellerRating()));
    }

    @Test
    public void testModelToDto_nullInput() {
        assertNull(mapper.toDTO(null));
    }

    // ── Domain Model → JPA Entity ──────────────────────────────────────────

    @Test
    public void testModelToEntity() {
        Offer model = createSampleModel();
        OfferEntity entity = mapper.toEntity(model);

        assertEquals("offer-123", entity.getId());
        assertEquals("product-001", entity.getProductId());
        assertEquals("seller-001", entity.getSupplierId());
        assertEquals("DEAL", entity.getOfferType());
        assertEquals("Summer Sale", entity.getTitle());
        assertEquals(0, new BigDecimal("100.00").compareTo(entity.getOriginalPrice()));
        assertEquals(0, new BigDecimal("80.00").compareTo(entity.getOfferPrice()));
        assertEquals(0, new BigDecimal("20.00").compareTo(entity.getDiscountPercent()));
        assertEquals(500, entity.getMaxQuantity());
        assertEquals(10, entity.getSoldQuantity());
    }

    @Test
    public void testModelToEntity_nullInput() {
        assertNull(mapper.toEntity(null));
    }

    // ── JPA Entity → Domain Model ──────────────────────────────────────────

    @Test
    public void testEntityToModel() {
        OfferEntity entity = createSampleEntity();
        Offer model = mapper.toModel(entity);

        assertEquals("offer-123", model.getId());
        assertEquals("product-001", model.getProductId());
        assertEquals("seller-001", model.getSupplierId());
        assertEquals(OfferType.DEAL, model.getOfferType());
        assertEquals("Summer Sale", model.getTitle());
        assertEquals(0, new BigDecimal("100.00").compareTo(model.getOriginalPrice()));
        assertEquals(0, new BigDecimal("80.00").compareTo(model.getOfferPrice()));
    }

    @Test
    public void testEntityToModel_nullInput() {
        assertNull(mapper.toModel((OfferEntity) null));
    }

    // ── Round-trip: DTO → Model → Entity → Model → DTO ────────────────────

    @Test
    public void testRoundTrip() {
        OfferDTO originalDto = createSampleDTO();

        // DTO -> Model -> Entity -> Model -> DTO
        Offer model1 = mapper.toModel(originalDto);
        OfferEntity entity = mapper.toEntity(model1);
        Offer model2 = mapper.toModel(entity);
        OfferDTO resultDto = mapper.toDTO(model2);

        assertEquals(originalDto.getId(), resultDto.getId());
        assertEquals(originalDto.getProductId(), resultDto.getProductId());
        assertEquals(originalDto.getSupplierId(), resultDto.getSupplierId());
        assertEquals(originalDto.getOfferType(), resultDto.getOfferType());
        assertEquals(originalDto.getTitle(), resultDto.getTitle());
        assertEquals(originalDto.getDescription(), resultDto.getDescription());
        assertEquals(0, originalDto.getOriginalPrice().compareTo(resultDto.getOriginalPrice()));
        assertEquals(0, originalDto.getOfferPrice().compareTo(resultDto.getOfferPrice()));
        assertEquals(0, originalDto.getDiscountPercent().compareTo(resultDto.getDiscountPercent()));
        assertEquals(originalDto.getMaxQuantity(), resultDto.getMaxQuantity());
        assertEquals(originalDto.getSoldQuantity(), resultDto.getSoldQuantity());
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private OfferDTO createSampleDTO() {
        OfferDTO dto = new OfferDTO();
        dto.setId("offer-123");
        dto.setProductId("product-001");
        dto.setSupplierId("seller-001");
        dto.setOfferType("DEAL");
        dto.setTitle("Summer Sale");
        dto.setDescription("Big discount on mugs");
        dto.setOriginalPrice(new BigDecimal("100.00"));
        dto.setOfferPrice(new BigDecimal("80.00"));
        dto.setDiscountPercent(new BigDecimal("20.00"));
        dto.setStartDate(new Date());
        dto.setEndDate(new Date(System.currentTimeMillis() + 86400000L * 30));
        dto.setMaxQuantity(500);
        dto.setSoldQuantity(10);
        dto.setSellerRating(new BigDecimal("4.50"));
        return dto;
    }

    private Offer createSampleModel() {
        Offer model = new Offer();
        model.setId("offer-123");
        model.setProductId("product-001");
        model.setSupplierId("seller-001");
        model.setOfferType(OfferType.DEAL);
        model.setTitle("Summer Sale");
        model.setDescription("Big discount on mugs");
        model.setOriginalPrice(new BigDecimal("100.00"));
        model.setOfferPrice(new BigDecimal("80.00"));
        model.setDiscountPercent(new BigDecimal("20.00"));
        model.setStartDate(new Date());
        model.setEndDate(new Date(System.currentTimeMillis() + 86400000L * 30));
        model.setMaxQuantity(500);
        model.setSoldQuantity(10);
        model.setSellerRating(new BigDecimal("4.50"));
        return model;
    }

    private OfferEntity createSampleEntity() {
        OfferEntity entity = new OfferEntity();
        entity.setId("offer-123");
        entity.setProductId("product-001");
        entity.setSupplierId("seller-001");
        entity.setOfferType("DEAL");
        entity.setTitle("Summer Sale");
        entity.setDescription("Big discount on mugs");
        entity.setOriginalPrice(new BigDecimal("100.00"));
        entity.setOfferPrice(new BigDecimal("80.00"));
        entity.setDiscountPercent(new BigDecimal("20.00"));
        entity.setStartDate(new Date());
        entity.setEndDate(new Date(System.currentTimeMillis() + 86400000L * 30));
        entity.setMaxQuantity(500);
        entity.setSoldQuantity(10);
        entity.setSellerRating(new BigDecimal("4.50"));
        return entity;
    }
}

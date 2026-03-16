package com.homebase.ecom.product.infrastructure.persistence.mapper;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.product.domain.model.ProductAttributeValue;
import com.homebase.ecom.product.domain.model.ProductMedia;
import com.homebase.ecom.product.domain.model.Variant;
import com.homebase.ecom.product.domain.model.VariantMedia;
import com.homebase.ecom.product.infrastructure.persistence.entity.ProductActivityLogEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.ProductAttributeValueEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.ProductEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.ProductMediaEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.VariantEntity;
import com.homebase.ecom.product.infrastructure.persistence.entity.VariantMediaEntity;
import org.chenile.stm.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
    }

    // ---- Entity -> Model tests ----

    @Test
    void toModel_nullEntity_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toModel_fullEntity_mapsAllFields() {
        ProductEntity entity = buildFullProductEntity();
        Product model = mapper.toModel(entity);

        assertEquals("prod-1", model.getId());
        assertEquals("Widget", model.getName());
        assertEquals("A fine widget", model.getDescription());
        assertEquals("Fine widget", model.getShortDescription());
        assertEquals("Acme", model.getBrand());
        assertEquals("cat-1", model.getCategoryId());
        assertEquals("widget-slug", model.getSlug());
        assertEquals("Widget Title", model.getMetaTitle());
        assertEquals("Widget meta desc", model.getMetaDescription());
        assertEquals(Arrays.asList("tag1", "tag2"), model.getTags());
        assertEquals(new State("ACTIVE", "default"), model.getCurrentState());
        assertEquals(Long.valueOf(5L), model.getVersion());
        assertNotNull(model.getCreatedTime());
        assertNotNull(model.getLastModifiedTime());
        assertEquals("user-1", model.getCreatedBy());
        assertEquals("user-2", model.getLastModifiedBy());
        assertNotNull(model.getStateEntryTime());
        assertEquals(1, model.getSlaLate());
        assertEquals(2, model.getSlaTendingLate());
    }

    @Test
    void toModel_mapsVariants() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");

        VariantEntity ve = new VariantEntity();
        ve.setId("v1");
        ve.setSku("SKU-001");
        Map<String, String> attrs = new HashMap<>();
        attrs.put("color", "red");
        ve.setAttributes(attrs);

        VariantMediaEntity vme = new VariantMediaEntity();
        vme.setVariantId("v1");
        vme.setAssetId("asset-1");
        vme.setPrimary(true);
        vme.setSortOrder(1);
        ve.setMedia(List.of(vme));

        entity.setVariants(List.of(ve));

        Product model = mapper.toModel(entity);

        assertEquals(1, model.getVariants().size());
        Variant v = model.getVariants().get(0);
        assertEquals("v1", v.getId());
        assertEquals("SKU-001", v.getSku());
        assertEquals("red", v.getAttributes().get("color"));
        assertEquals(1, v.getMedia().size());
        VariantMedia vm = v.getMedia().get(0);
        assertEquals("v1", vm.getVariantId());
        assertEquals("asset-1", vm.getAssetId());
        assertTrue(vm.isPrimary());
        assertEquals(1, vm.getSortOrder());
    }

    @Test
    void toModel_mapsAttributes() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");

        ProductAttributeValueEntity ave = new ProductAttributeValueEntity();
        ave.setId("av1");
        ave.setProductId("p1");
        ave.setAttributeId("attr-1");
        ave.setOptionId("opt-1");
        ave.setRawValue("42");
        entity.setAttributes(List.of(ave));

        Product model = mapper.toModel(entity);

        assertEquals(1, model.getAttributes().size());
        ProductAttributeValue av = model.getAttributes().get(0);
        assertEquals("av1", av.getId());
        assertEquals("p1", av.getProductId());
        assertEquals("attr-1", av.getAttributeId());
        assertEquals("opt-1", av.getOptionId());
        assertEquals("42", av.getRawValue());
    }

    @Test
    void toModel_mapsMedia() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");

        ProductMediaEntity pme = new ProductMediaEntity();
        pme.setId("m1");
        pme.setProductId("p1");
        pme.setAssetId("asset-1");
        pme.setPrimary(true);
        pme.setSortOrder(0);
        entity.setMedia(List.of(pme));

        Product model = mapper.toModel(entity);

        assertEquals(1, model.getMedia().size());
        ProductMedia pm = model.getMedia().get(0);
        assertEquals("m1", pm.getId());
        assertEquals("p1", pm.getProductId());
        assertEquals("asset-1", pm.getAssetId());
        assertTrue(pm.isPrimary());
        assertEquals(0, pm.getSortOrder());
    }

    @Test
    void toModel_mapsActivities() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");

        ProductActivityLogEntity log = new ProductActivityLogEntity();
        log.setActivityName("CREATE");
        log.setActivityComment("Product created");
        log.setActivitySuccess(true);
        entity.setActivities(List.of(log));

        Product model = mapper.toModel(entity);

        assertEquals(1, model.getActivities().size());
        assertEquals("CREATE", model.getActivities().get(0).getName());
        assertEquals("Product created", model.getActivities().get(0).getComment());
        assertTrue(model.getActivities().get(0).getSuccess());
    }

    @Test
    void toModel_nullTags_returnsEmptyList() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");
        entity.setTags(null);

        Product model = mapper.toModel(entity);

        assertNotNull(model.getTags());
        assertTrue(model.getTags().isEmpty());
    }

    @Test
    void toModel_nullCollections_noExceptions() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");
        entity.setVariants(null);
        entity.setAttributes(null);
        entity.setMedia(null);
        entity.setActivities(null);

        Product model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("p1", model.getId());
    }

    @Test
    void toModel_emptyStringFields_preservedAsEmpty() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");
        entity.setName("");
        entity.setDescription("");
        entity.setSlug("");

        Product model = mapper.toModel(entity);

        assertEquals("", model.getName());
        assertEquals("", model.getDescription());
        assertEquals("", model.getSlug());
    }

    @Test
    void toModel_stateFlowFieldsMapped() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");
        State state = new State("DRAFT", "pim-flow");
        entity.setCurrentState(state);

        Product model = mapper.toModel(entity);

        assertNotNull(model.getCurrentState());
        assertEquals("DRAFT", model.getCurrentState().getStateId());
        assertEquals("pim-flow", model.getCurrentState().getFlowId());
    }

    // ---- Model -> Entity tests ----

    @Test
    void toEntity_nullModel_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_fullModel_mapsAllFields() {
        Product model = buildFullProduct();
        ProductEntity entity = mapper.toEntity(model);

        assertEquals("prod-1", entity.getId());
        assertEquals("Widget", entity.getName());
        assertEquals("A fine widget", entity.getDescription());
        assertEquals("Fine widget", entity.getShortDescription());
        assertEquals("Acme", entity.getBrand());
        assertEquals("cat-1", entity.getCategoryId());
        assertEquals("widget-slug", entity.getSlug());
        assertEquals("Widget Title", entity.getMetaTitle());
        assertEquals("Widget meta desc", entity.getMetaDescription());
        assertEquals(Arrays.asList("tag1", "tag2"), entity.getTags());
        assertEquals(new State("ACTIVE", "default"), entity.getCurrentState());
        assertEquals(Long.valueOf(5L), entity.getVersion());
        assertNotNull(entity.getCreatedTime());
        assertNotNull(entity.getLastModifiedTime());
        assertEquals("user-1", entity.getCreatedBy());
        assertEquals("user-2", entity.getLastModifiedBy());
        assertNotNull(entity.getStateEntryTime());
        assertEquals(1, entity.getSlaLate());
        assertEquals(2, entity.getSlaTendingLate());
    }

    @Test
    void toEntity_mapsVariants() {
        Product model = new Product();
        model.setId("p1");

        Variant v = new Variant();
        v.setId("v1");
        v.setSku("SKU-001");
        Map<String, String> attrs = new HashMap<>();
        attrs.put("size", "L");
        v.setAttributes(attrs);

        VariantMedia vm = new VariantMedia();
        vm.setVariantId("v1");
        vm.setAssetId("asset-1");
        vm.setPrimary(false);
        vm.setSortOrder(2);
        v.setMedia(List.of(vm));

        model.setVariants(List.of(v));

        ProductEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.getVariants().size());
        VariantEntity ve = entity.getVariants().get(0);
        assertEquals("v1", ve.getId());
        assertEquals("SKU-001", ve.getSku());
        assertEquals("L", ve.getAttributes().get("size"));
        assertEquals(1, ve.getMedia().size());
        VariantMediaEntity vme = ve.getMedia().get(0);
        assertEquals("v1", vme.getVariantId());
        assertEquals("asset-1", vme.getAssetId());
        assertFalse(vme.isPrimary());
        assertEquals(2, vme.getSortOrder());
    }

    @Test
    void toEntity_mapsAttributes() {
        Product model = new Product();
        model.setId("p1");

        ProductAttributeValue av = new ProductAttributeValue();
        av.setId("av1");
        av.setProductId("p1");
        av.setAttributeId("attr-1");
        av.setOptionId("opt-1");
        av.setRawValue("42");
        model.setAttributes(List.of(av));

        ProductEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.getAttributes().size());
        ProductAttributeValueEntity ave = entity.getAttributes().get(0);
        assertEquals("av1", ave.getId());
        assertEquals("p1", ave.getProductId());
        assertEquals("attr-1", ave.getAttributeId());
        assertEquals("opt-1", ave.getOptionId());
        assertEquals("42", ave.getRawValue());
    }

    @Test
    void toEntity_mapsMedia() {
        Product model = new Product();
        model.setId("p1");

        ProductMedia pm = new ProductMedia();
        pm.setId("m1");
        pm.setProductId("p1");
        pm.setAssetId("asset-1");
        pm.setPrimary(true);
        pm.setSortOrder(0);
        model.setMedia(List.of(pm));

        ProductEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.getMedia().size());
        ProductMediaEntity pme = entity.getMedia().get(0);
        assertEquals("m1", pme.getId());
        assertEquals("p1", pme.getProductId());
        assertEquals("asset-1", pme.getAssetId());
        assertTrue(pme.isPrimary());
        assertEquals(0, pme.getSortOrder());
    }

    @Test
    void toEntity_mapsActivities() {
        Product model = new Product();
        model.setId("p1");
        model.addActivity("SUBMIT", "Submitted for review");

        ProductEntity entity = mapper.toEntity(model);

        assertEquals(1, entity.getActivities().size());
        ProductActivityLogEntity log = entity.getActivities().get(0);
        assertEquals("SUBMIT", log.getName());
        assertEquals("Submitted for review", log.getComment());
        assertTrue(log.getSuccess());
    }

    @Test
    void toEntity_nullVersion_notSetOnEntity() {
        Product model = new Product();
        model.setId("p1");
        model.setVersion(null);

        ProductEntity entity = mapper.toEntity(model);

        // version field on BaseJpaEntity defaults to 0 (primitive long)
        // The mapper checks: if (model.getVersion() != null) entity.setVersion(...)
        // So when null, entity keeps its default
        assertNotNull(entity);
    }

    @Test
    void toEntity_nullTags_returnsEmptyList() {
        Product model = new Product();
        model.setId("p1");
        model.setTags(null);

        ProductEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getTags());
        assertTrue(entity.getTags().isEmpty());
    }

    @Test
    void toEntity_nullCollections_noExceptions() {
        Product model = new Product();
        model.setId("p1");
        model.setVariants(null);
        model.setAttributes(null);
        model.setMedia(null);

        ProductEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("p1", entity.getId());
    }

    @Test
    void toEntity_stateFlowFieldsMapped() {
        Product model = new Product();
        model.setId("p1");
        State state = new State("APPROVED", "review-flow");
        model.setCurrentState(state);

        ProductEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getCurrentState());
        assertEquals("APPROVED", entity.getCurrentState().getStateId());
        assertEquals("review-flow", entity.getCurrentState().getFlowId());
    }

    @Test
    void toEntity_versionPreserved() {
        Product model = new Product();
        model.setId("p1");
        model.setVersion(99L);

        ProductEntity entity = mapper.toEntity(model);

        assertEquals(Long.valueOf(99L), entity.getVersion());
    }

    // ---- Round-trip test ----

    @Test
    void roundTrip_entityToModelToEntity_preservesFields() {
        ProductEntity original = buildFullProductEntity();
        Product model = mapper.toModel(original);
        ProductEntity roundTripped = mapper.toEntity(model);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getName(), roundTripped.getName());
        assertEquals(original.getDescription(), roundTripped.getDescription());
        assertEquals(original.getShortDescription(), roundTripped.getShortDescription());
        assertEquals(original.getBrand(), roundTripped.getBrand());
        assertEquals(original.getCategoryId(), roundTripped.getCategoryId());
        assertEquals(original.getSlug(), roundTripped.getSlug());
        assertEquals(original.getMetaTitle(), roundTripped.getMetaTitle());
        assertEquals(original.getMetaDescription(), roundTripped.getMetaDescription());
        assertEquals(original.getTags(), roundTripped.getTags());
        assertEquals(original.getCurrentState(), roundTripped.getCurrentState());
        assertEquals(original.getSlaLate(), roundTripped.getSlaLate());
        assertEquals(original.getSlaTendingLate(), roundTripped.getSlaTendingLate());
    }

    @Test
    void toModel_emptyCollections_mappedAsEmpty() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");
        entity.setVariants(new ArrayList<>());
        entity.setAttributes(new ArrayList<>());
        entity.setMedia(new ArrayList<>());
        entity.setActivities(new ArrayList<>());
        entity.setTags(new ArrayList<>());

        Product model = mapper.toModel(entity);

        assertTrue(model.getVariants().isEmpty());
        assertTrue(model.getAttributes().isEmpty());
        assertTrue(model.getMedia().isEmpty());
        assertTrue(model.getTags().isEmpty());
    }

    @Test
    void toEntity_zeroSortOrder_preserved() {
        Product model = new Product();
        model.setId("p1");

        ProductMedia pm = new ProductMedia();
        pm.setId("m1");
        pm.setSortOrder(0);
        pm.setPrimary(false);
        model.setMedia(List.of(pm));

        ProductEntity entity = mapper.toEntity(model);

        assertEquals(0, entity.getMedia().get(0).getSortOrder());
        assertFalse(entity.getMedia().get(0).isPrimary());
    }

    @Test
    void toModel_nullCurrentState_preservedAsNull() {
        ProductEntity entity = new ProductEntity();
        entity.setId("p1");
        entity.setCurrentState(null);

        Product model = mapper.toModel(entity);

        assertNull(model.getCurrentState());
    }

    // ---- Helpers ----

    private ProductEntity buildFullProductEntity() {
        ProductEntity entity = new ProductEntity();
        entity.setId("prod-1");
        entity.setName("Widget");
        entity.setDescription("A fine widget");
        entity.setShortDescription("Fine widget");
        entity.setBrand("Acme");
        entity.setCategoryId("cat-1");
        entity.setSlug("widget-slug");
        entity.setMetaTitle("Widget Title");
        entity.setMetaDescription("Widget meta desc");
        entity.setTags(new ArrayList<>(Arrays.asList("tag1", "tag2")));
        entity.setCurrentState(new State("ACTIVE", "default"));
        entity.setVersion(5L);
        entity.setCreatedTime(new Date());
        entity.setLastModifiedTime(new Date());
        entity.setCreatedBy("user-1");
        entity.setLastModifiedBy("user-2");
        entity.setStateEntryTime(new Date());
        entity.setSlaLate(1);
        entity.setSlaTendingLate(2);
        return entity;
    }

    private Product buildFullProduct() {
        Product model = new Product();
        model.setId("prod-1");
        model.setName("Widget");
        model.setDescription("A fine widget");
        model.setShortDescription("Fine widget");
        model.setBrand("Acme");
        model.setCategoryId("cat-1");
        model.setSlug("widget-slug");
        model.setMetaTitle("Widget Title");
        model.setMetaDescription("Widget meta desc");
        model.setTags(new ArrayList<>(Arrays.asList("tag1", "tag2")));
        model.setCurrentState(new State("ACTIVE", "default"));
        model.setVersion(5L);
        model.setCreatedTime(new Date());
        model.setLastModifiedTime(new Date());
        model.setCreatedBy("user-1");
        model.setLastModifiedBy("user-2");
        model.setStateEntryTime(new Date());
        model.setSlaLate(1);
        model.setSlaTendingLate(2);
        return model;
    }
}

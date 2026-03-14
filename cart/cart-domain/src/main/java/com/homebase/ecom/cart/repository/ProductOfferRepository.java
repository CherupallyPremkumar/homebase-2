package com.homebase.ecom.cart.repository;

import java.util.List;

import com.homebase.ecom.dto.OfferDto;
import org.chenile.query.annotation.ChenileParam;
import org.chenile.query.annotation.ChenileRepositoryDefinition;
import org.chenile.query.annotation.QueryName;
import org.chenile.query.repository.ChenileRepository;

/**
 * ChenileRepository for reading ProductOffer data via the product-query
 * service.
 *
 * Query mapping (from offer.json + offer.xml):
 * "offers" → Offer.getByProductId (paginated list)
 * "offersByProduct" → Offer.getByProductId (single record by productId)
 *
 * Used by AbstractCartAction to validate product availability and price before
 * adding an item to the cart.
 */
@ChenileRepositoryDefinition(entityClass = OfferDto.class)
public interface ProductOfferRepository extends ChenileRepository<OfferDto> {

    /**
     * Fetch the first active offer for a given productId.
     * Maps to Offer.getByProductId → name: "offers" in offer.json.
     */
    @QueryName("offers")
    OfferDto findById(@ChenileParam("productId") String productId);

    /**
     * List all offers for a product (including inactive).
     * Maps to Offer.getByProductId → name: "offers" in offer.json.
     */
    @QueryName("offers")
    List<OfferDto> findByProductId(@ChenileParam("productId") String productId);

    /**
     * List only active offers for a product.
     * Maps to Offer.getByProductId → name: "offers" with active filter.
     */
    @QueryName("offers")
    List<OfferDto> findActiveByProductId(
            @ChenileParam("productId") String productId,
            @ChenileParam("active") Boolean active);
}

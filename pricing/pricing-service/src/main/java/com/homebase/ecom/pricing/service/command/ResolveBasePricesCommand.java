package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.LineItemPricing;
import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.domain.port.OfferPricePort;
import com.homebase.ecom.pricing.domain.port.OfferPricePort.OfferPrice;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Step 1: Resolve base prices for each line item from the Offer module.
 *
 * In Amazon's model, the Offer is the single source of truth for prices.
 * Pricing NEVER trusts prices sent by the caller — it always fetches
 * the authoritative price from the Offer module via OfferPricePort.
 *
 * If Offer module is unavailable and cart sent a basePrice, falls back to it
 * (logged as a warning — this is a degraded mode, not normal operation).
 */
public class ResolveBasePricesCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(ResolveBasePricesCommand.class);
    private final OfferPricePort offerPricePort;

    public ResolveBasePricesCommand(OfferPricePort offerPricePort) {
        this.offerPricePort = offerPricePort;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        String currency = ctx.getCurrency();
        Money subtotal = Money.zero(currency);

        for (LineItemPricing item : ctx.getLineItems()) {
            Money basePrice = resolvePrice(item, currency);
            if (basePrice == null) {
                ctx.setError(true);
                ctx.setErrorMessage("No price available for variant: " + item.getVariantId());
                return;
            }

            item.setUnitPrice(basePrice);
            item.setCurrentPrice(basePrice);
            Money lineSub = basePrice.multiply(item.getQuantity());
            item.setLineSubtotal(lineSub);
            item.setLineDiscount(Money.zero(currency));
            subtotal = subtotal.add(lineSub);
        }

        ctx.setSubtotal(subtotal);
        ctx.setTotalDiscount(Money.zero(currency));
        log.debug("Resolved base prices from Offer. Subtotal: {}", subtotal.toDisplayString());
    }

    private Money resolvePrice(LineItemPricing item, String currency) {
        // Fetch authoritative price from Offer module
        Optional<OfferPrice> offerPrice = offerPricePort.getOfferPrice(
                item.getVariantId(), item.getSellerId(), currency);

        if (offerPrice.isPresent()) {
            OfferPrice offer = offerPrice.get();
            log.debug("Offer price for variant {}: listing={}, sale={}, buyBox={}",
                    item.getVariantId(),
                    offer.listingPrice().toDisplayString(),
                    offer.salePrice() != null ? offer.salePrice().toDisplayString() : "none",
                    offer.buyBoxWinner());
            // Use listing price as base. Sale/promo price applied in ApplyPromoPriceCommand.
            return offer.listingPrice();
        }

        // Degraded fallback: use price from cart snapshot if Offer is unavailable
        if (item.getUnitPrice() != null) {
            log.warn("Offer unavailable for variant {}. Using cart snapshot basePrice as fallback.", item.getVariantId());
            return item.getUnitPrice();
        }

        return null;
    }
}

package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.DiscountResult;
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
 * Step 3: Apply promotional/sale prices from active offers.
 * If the Offer has an active salePrice lower than the listing price,
 * apply the difference as a PROMO discount.
 *
 * This is separate from ResolveBasePrices because:
 * - Step 1 sets the listing price as base
 * - Step 2 applies rules-engine discounts (volume, tier, flash-sale)
 * - Step 3 (this) applies the Offer sale price if it's better than the discounted price
 * The customer gets the better deal.
 */
public class ApplyPromoPriceCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(ApplyPromoPriceCommand.class);
    private final OfferPricePort offerPricePort;

    public ApplyPromoPriceCommand(OfferPricePort offerPricePort) {
        this.offerPricePort = offerPricePort;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        if (ctx.shouldSkip("applyPromoPrice")) return;

        for (LineItemPricing item : ctx.getLineItems()) {
            Optional<OfferPrice> offerOpt = offerPricePort.getOfferPrice(
                    item.getVariantId(), item.getSellerId(), ctx.getCurrency());

            if (offerOpt.isEmpty() || !offerOpt.get().hasActivePromo()) continue;

            OfferPrice offer = offerOpt.get();
            Money salePrice = offer.salePrice();

            // Only apply if sale price is better than current discounted price
            if (salePrice.isLessThan(item.getCurrentPrice())) {
                Money discount = item.getCurrentPrice().subtract(salePrice);
                Money totalDiscount = discount.multiply(item.getQuantity());

                item.setCurrentPrice(salePrice);
                item.setLineDiscount(item.getLineDiscount().add(totalDiscount));
                item.addDiscount(new DiscountResult(totalDiscount, "PROMO",
                        "Sale price: " + salePrice.toDisplayString(), 0));

                log.debug("Promo price on variant {}: {} -> {}",
                        item.getVariantId(), item.getUnitPrice().toDisplayString(),
                        salePrice.toDisplayString());
            }
        }
    }
}

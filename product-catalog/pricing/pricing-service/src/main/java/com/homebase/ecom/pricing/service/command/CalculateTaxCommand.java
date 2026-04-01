package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.domain.port.TaxCalculationPort;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step 6: Calculate tax (GST) on taxable amount.
 * taxableAmount = subtotal - totalDiscount
 * Delegates to TaxCalculationPort (hexagonal — domain port, infrastructure adapter).
 *
 * TODO: Integrate with dedicated Tax module when ready:
 *   - Replace default GST-rate stub with TaxServiceAdapter calling Tax BC via ProxyBuilder
 *   - Support HSN-code-based rates (Tax module owns HSN→rate mapping)
 *   - Support category-based rates (pass productCategory from LineItemPricing)
 *   - Support region/state-specific tax rules (IGST vs CGST+SGST)
 *   - Support tax-exempt items and zero-rated exports
 */
public class CalculateTaxCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(CalculateTaxCommand.class);
    private final TaxCalculationPort taxCalculationPort;

    public CalculateTaxCommand(TaxCalculationPort taxCalculationPort) {
        this.taxCalculationPort = taxCalculationPort;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        if (ctx.shouldSkip("calculateTax")) return;

        Money taxableAmount = ctx.getSubtotal().subtract(ctx.getTotalDiscount());
        if (taxableAmount.isNegative()) {
            taxableAmount = Money.zero(ctx.getCurrency());
        }

        Money tax = taxCalculationPort.calculateTax(taxableAmount, ctx.getRegion(), null);
        ctx.setTaxAmount(tax);

        if (ctx.getShippingCost() == null) {
            ctx.setShippingCost(Money.zero(ctx.getCurrency()));
        }

        log.debug("Tax calculated: taxable={}, tax={}",
                taxableAmount.toDisplayString(), tax.toDisplayString());
    }
}

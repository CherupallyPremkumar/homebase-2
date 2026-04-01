package com.homebase.ecom.tax.service.command;

import com.homebase.ecom.tax.model.TaxContext;
import com.homebase.ecom.tax.model.TaxRate;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * OWIZ Step 4: Look up GST rate for each HSN code.
 * HSN code → tax_rates table → gstRate + cessRate
 */
public class LookupTaxRatesCommand implements Command<TaxContext> {

    private static final Logger log = LoggerFactory.getLogger(LookupTaxRatesCommand.class);
    private static final BigDecimal DEFAULT_GST_RATE = new BigDecimal("18");

    private final TaxRateRepository taxRateRepository;

    public LookupTaxRatesCommand(TaxRateRepository taxRateRepository) {
        this.taxRateRepository = taxRateRepository;
    }

    @Override
    public void execute(TaxContext ctx) throws Exception {
        if (ctx.isError()) return;

        for (var entry : ctx.getResolvedHsnCodes().entrySet()) {
            String variantId = entry.getKey();
            String hsnCode = entry.getValue();

            Optional<TaxRate> taxRate = taxRateRepository.findActiveByHsnCode(hsnCode);

            if (taxRate.isPresent()) {
                TaxRate rate = taxRate.get();
                ctx.getTaxRates().put(variantId, rate.getGstRate());
                if (rate.getCessRate() != null && rate.getCessRate().compareTo(BigDecimal.ZERO) > 0) {
                    ctx.getCessRates().put(variantId, rate.getCessRate());
                }
                log.debug("Tax rate for variant {}: hsn={}, gst={}%, cess={}%",
                        variantId, hsnCode, rate.getGstRate(), rate.getCessRate());
            } else {
                ctx.getTaxRates().put(variantId, DEFAULT_GST_RATE);
                log.warn("No active tax rate for HSN {}, using default {}%", hsnCode, DEFAULT_GST_RATE);
            }
        }
    }
}

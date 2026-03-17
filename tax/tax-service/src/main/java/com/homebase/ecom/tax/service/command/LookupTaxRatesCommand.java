package com.homebase.ecom.tax.service.command;

import com.homebase.ecom.tax.model.TaxCategoryMapping;
import com.homebase.ecom.tax.model.TaxContext;
import com.homebase.ecom.tax.model.TaxRate;
import com.homebase.ecom.tax.model.port.TaxCategoryMappingRepository;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Step 3: Look up tax rate for each HSN code.
 * HSN code → tax_category_mapping → tax_rate_id → tax_rates.rate
 * Also resolves cess rates for luxury categories.
 */
public class LookupTaxRatesCommand implements Command<TaxContext> {

    private static final Logger log = LoggerFactory.getLogger(LookupTaxRatesCommand.class);
    private static final BigDecimal DEFAULT_RATE = new BigDecimal("18");

    private final TaxCategoryMappingRepository categoryMappingRepository;
    private final TaxRateRepository taxRateRepository;

    public LookupTaxRatesCommand(TaxCategoryMappingRepository categoryMappingRepository,
                                  TaxRateRepository taxRateRepository) {
        this.categoryMappingRepository = categoryMappingRepository;
        this.taxRateRepository = taxRateRepository;
    }

    @Override
    public void execute(TaxContext ctx) throws Exception {
        if (ctx.isError()) return;

        for (var entry : ctx.getResolvedHsnCodes().entrySet()) {
            String variantId = entry.getKey();
            String hsnCode = entry.getValue();

            BigDecimal rate = resolveRate(hsnCode);
            ctx.getTaxRates().put(variantId, rate);

            // Check for cess (luxury items typically HSN 7113, 7114, etc.)
            BigDecimal cessRate = resolveCessRate(hsnCode);
            if (cessRate != null && cessRate.compareTo(BigDecimal.ZERO) > 0) {
                ctx.getCessRates().put(variantId, cessRate);
            }

            log.debug("Tax rate for variant {}: hsn={}, rate={}%, cess={}%",
                    variantId, hsnCode, rate, cessRate);
        }
    }

    private BigDecimal resolveRate(String hsnCode) {
        Optional<TaxCategoryMapping> mapping = categoryMappingRepository.findByHsnCode(hsnCode);
        if (mapping.isPresent()) {
            Optional<TaxRate> taxRate = taxRateRepository.findById(mapping.get().getTaxRateId());
            if (taxRate.isPresent() && taxRate.get().isActive()) {
                return taxRate.get().getRate();
            }
        }
        log.warn("No active tax rate for HSN {}, using default {}%", hsnCode, DEFAULT_RATE);
        return DEFAULT_RATE;
    }

    private BigDecimal resolveCessRate(String hsnCode) {
        // Cess rates are stored as separate tax_rates with taxType = "CESS"
        // and linked via tax_category_mapping with a cess-specific mapping
        // For now, cess is identified by HSN code prefix for luxury items
        if (hsnCode != null && (hsnCode.startsWith("71") || hsnCode.startsWith("87"))) {
            return new BigDecimal("1"); // 1% cess on luxury/automobiles
        }
        return null;
    }
}

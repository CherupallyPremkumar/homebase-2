package com.homebase.ecom.tax.service.command;

import com.homebase.ecom.tax.model.TaxContext;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Step 4: Calculate tax amounts for each item.
 * INTRA_STATE: CGST = rate/2, SGST = rate/2
 * INTER_STATE: IGST = full rate
 * Plus cess if applicable.
 * All amounts in minor units (paise).
 */
public class CalculateTaxAmountsCommand implements Command<TaxContext> {

    private static final Logger log = LoggerFactory.getLogger(CalculateTaxAmountsCommand.class);
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal TWO = new BigDecimal("2");

    @Override
    public void execute(TaxContext ctx) throws Exception {
        if (ctx.isError()) return;

        long totalTax = 0;

        for (TaxContext.TaxableItem item : ctx.getItems()) {
            BigDecimal rate = ctx.getTaxRates().getOrDefault(item.getVariantId(), BigDecimal.ZERO);
            BigDecimal cessRate = ctx.getCessRates().getOrDefault(item.getVariantId(), BigDecimal.ZERO);
            BigDecimal taxableAmount = BigDecimal.valueOf(item.getTaxableAmount());

            TaxContext.TaxLineResult result = new TaxContext.TaxLineResult();
            result.setVariantId(item.getVariantId());
            result.setHsnCode(ctx.getResolvedHsnCodes().get(item.getVariantId()));
            result.setTaxableAmount(item.getTaxableAmount());
            result.setRate(rate);
            result.setCessRate(cessRate);

            if (ctx.getTaxType() == TaxContext.TaxType.INTRA_STATE) {
                // CGST = taxableAmount * (rate/2) / 100
                BigDecimal halfRate = rate.divide(TWO, 4, RoundingMode.HALF_UP);
                long cgst = taxableAmount.multiply(halfRate).divide(HUNDRED, 0, RoundingMode.HALF_UP).longValue();
                long sgst = cgst; // symmetric
                result.setCgstAmount(cgst);
                result.setSgstAmount(sgst);
                result.setIgstAmount(0);
            } else {
                // IGST = taxableAmount * rate / 100
                long igst = taxableAmount.multiply(rate).divide(HUNDRED, 0, RoundingMode.HALF_UP).longValue();
                result.setIgstAmount(igst);
                result.setCgstAmount(0);
                result.setSgstAmount(0);
            }

            // Cess
            long cess = 0;
            if (cessRate.compareTo(BigDecimal.ZERO) > 0) {
                cess = taxableAmount.multiply(cessRate).divide(HUNDRED, 0, RoundingMode.HALF_UP).longValue();
            }
            result.setCessAmount(cess);

            long itemTax = result.getCgstAmount() + result.getSgstAmount() + result.getIgstAmount() + cess;
            result.setTotalTax(itemTax);
            totalTax += itemTax;

            ctx.getTaxLineResults().add(result);

            log.debug("Tax for {}: taxable={}, rate={}%, cgst={}, sgst={}, igst={}, cess={}, total={}",
                    item.getVariantId(), item.getTaxableAmount(), rate,
                    result.getCgstAmount(), result.getSgstAmount(), result.getIgstAmount(), cess, itemTax);
        }

        ctx.setTotalTax(totalTax);
    }
}

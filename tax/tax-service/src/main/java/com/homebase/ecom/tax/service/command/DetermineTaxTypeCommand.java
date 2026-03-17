package com.homebase.ecom.tax.service.command;

import com.homebase.ecom.tax.model.TaxContext;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step 2: Determine INTRA_STATE vs INTER_STATE.
 * Same state → CGST + SGST; Different state → IGST.
 */
public class DetermineTaxTypeCommand implements Command<TaxContext> {

    private static final Logger log = LoggerFactory.getLogger(DetermineTaxTypeCommand.class);

    @Override
    public void execute(TaxContext ctx) throws Exception {
        if (ctx.isError()) return;

        String source = normalize(ctx.getSourceState());
        String destination = normalize(ctx.getDestinationState());

        if (source.equals(destination)) {
            ctx.setTaxType(TaxContext.TaxType.INTRA_STATE);
            log.debug("Tax type: INTRA_STATE (source={}, destination={})", source, destination);
        } else {
            ctx.setTaxType(TaxContext.TaxType.INTER_STATE);
            log.debug("Tax type: INTER_STATE (source={}, destination={})", source, destination);
        }
    }

    private String normalize(String state) {
        if (state == null) return "";
        return state.trim().toUpperCase();
    }
}

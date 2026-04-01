package com.homebase.ecom.tax.service.command;

import com.homebase.ecom.tax.model.TaxContext;
import org.chenile.owiz.Command;

/**
 * Step 5: Assemble the final TaxContext output.
 * Marks the pipeline as complete. Response mapping happens in TaxServiceImpl.
 */
public class BuildTaxResponseCommand implements Command<TaxContext> {

    @Override
    public void execute(TaxContext ctx) throws Exception {
        if (ctx.isError()) return;
        // TaxContext already has all results populated by previous commands.
        // This step serves as the final validation checkpoint.
        if (ctx.getTaxLineResults().isEmpty()) {
            ctx.fail("No tax line results produced");
        }
    }
}

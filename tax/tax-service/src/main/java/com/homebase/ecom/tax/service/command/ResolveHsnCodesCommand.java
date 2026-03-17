package com.homebase.ecom.tax.service.command;

import com.homebase.ecom.tax.model.TaxCategoryMapping;
import com.homebase.ecom.tax.model.TaxContext;
import com.homebase.ecom.tax.model.port.TaxCategoryMappingRepository;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Step 1: Resolve HSN codes for each item.
 * If item already has hsnCode, use it. Otherwise, look up by productCategory.
 * Falls back to DEFAULT category if no mapping found.
 */
public class ResolveHsnCodesCommand implements Command<TaxContext> {

    private static final Logger log = LoggerFactory.getLogger(ResolveHsnCodesCommand.class);
    private static final String DEFAULT_HSN = "0000";

    private final TaxCategoryMappingRepository categoryMappingRepository;

    public ResolveHsnCodesCommand(TaxCategoryMappingRepository categoryMappingRepository) {
        this.categoryMappingRepository = categoryMappingRepository;
    }

    @Override
    public void execute(TaxContext ctx) throws Exception {
        if (ctx.isError()) return;

        for (TaxContext.TaxableItem item : ctx.getItems()) {
            String hsnCode = item.getHsnCode();

            if (hsnCode == null || hsnCode.isBlank()) {
                // Look up by product category
                String category = item.getProductCategory();
                if (category != null && !category.isBlank()) {
                    Optional<TaxCategoryMapping> mapping = categoryMappingRepository.findByCategoryId(category);
                    hsnCode = mapping.map(TaxCategoryMapping::getHsnCode).orElse(DEFAULT_HSN);
                } else {
                    hsnCode = DEFAULT_HSN;
                }
                log.debug("Resolved HSN for variant {}: category={} → hsn={}", item.getVariantId(), category, hsnCode);
            }

            ctx.getResolvedHsnCodes().put(item.getVariantId(), hsnCode);
        }
    }
}

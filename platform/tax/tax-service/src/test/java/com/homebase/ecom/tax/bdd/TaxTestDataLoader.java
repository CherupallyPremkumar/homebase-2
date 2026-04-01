package com.homebase.ecom.tax.bdd;

import com.homebase.ecom.tax.model.TaxCategoryMapping;
import com.homebase.ecom.tax.model.TaxRate;
import com.homebase.ecom.tax.model.port.TaxCategoryMappingRepository;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Seeds GST rate data into H2 for BDD tests.
 */
@Component
@Profile("unittest")
public class TaxTestDataLoader implements ApplicationRunner {

    private final TaxRateRepository taxRateRepository;
    private final TaxCategoryMappingRepository categoryMappingRepository;

    public TaxTestDataLoader(TaxRateRepository taxRateRepository,
                             TaxCategoryMappingRepository categoryMappingRepository) {
        this.taxRateRepository = taxRateRepository;
        this.categoryMappingRepository = categoryMappingRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        // GST Rate slabs by HSN code
        taxRateRepository.save(createRate("gst_0",  "0401", "Milk and dairy (essential)", BigDecimal.ZERO, BigDecimal.ZERO));
        taxRateRepository.save(createRate("gst_5",  "6109", "Cotton T-shirts",           new BigDecimal("5"), BigDecimal.ZERO));
        taxRateRepository.save(createRate("gst_12", "2106", "Food preparations",          new BigDecimal("12"), BigDecimal.ZERO));
        taxRateRepository.save(createRate("gst_18", "8471", "Computers and laptops",     new BigDecimal("18"), BigDecimal.ZERO));
        taxRateRepository.save(createRate("gst_28", "7113", "Gold jewellery",            new BigDecimal("28"), new BigDecimal("3")));
        taxRateRepository.save(createRate("gst_default", "0000", "Default rate",         new BigDecimal("18"), BigDecimal.ZERO));

        // Category → HSN code mappings
        categoryMappingRepository.save(createMapping("map_electronics", "ELECTRONICS", null, "8471", "Computers and electronics"));
        categoryMappingRepository.save(createMapping("map_clothing", "CLOTHING", null, "6109", "Apparel and garments"));
        categoryMappingRepository.save(createMapping("map_food", "FOOD", null, "2106", "Food and beverages"));
        categoryMappingRepository.save(createMapping("map_luxury", "LUXURY", null, "7113", "Jewellery and luxury"));
        categoryMappingRepository.save(createMapping("map_essential", "ESSENTIAL", null, "0401", "Essential goods"));
        categoryMappingRepository.save(createMapping("map_default", "DEFAULT", null, "0000", "Default category"));
    }

    private TaxRate createRate(String id, String hsnCode, String description, BigDecimal gstRate, BigDecimal cessRate) {
        TaxRate r = new TaxRate();
        r.setId(id);
        r.setHsnCode(hsnCode);
        r.setDescription(description);
        r.setGstRate(gstRate);
        r.setCessRate(cessRate);
        r.setTcsRate(new BigDecimal("1"));
        r.setEffectiveFrom(LocalDate.of(2017, 7, 1));
        r.setActive(true);
        return r;
    }

    private TaxCategoryMapping createMapping(String id, String productCategory, String subCategory, String hsnCode, String description) {
        TaxCategoryMapping m = new TaxCategoryMapping();
        m.setId(id);
        m.setProductCategory(productCategory);
        m.setSubCategory(subCategory);
        m.setHsnCode(hsnCode);
        m.setDescription(description);
        return m;
    }
}

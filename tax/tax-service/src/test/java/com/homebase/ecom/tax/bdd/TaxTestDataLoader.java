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
        // GST Rate slabs
        TaxRate gst0 = createRate("gst_0", "GST 0%", "IN", "GST", BigDecimal.ZERO);
        TaxRate gst5 = createRate("gst_5", "GST 5%", "IN", "GST", new BigDecimal("5"));
        TaxRate gst12 = createRate("gst_12", "GST 12%", "IN", "GST", new BigDecimal("12"));
        TaxRate gst18 = createRate("gst_18", "GST 18%", "IN", "GST", new BigDecimal("18"));
        TaxRate gst28 = createRate("gst_28", "GST 28%", "IN", "GST", new BigDecimal("28"));

        taxRateRepository.save(gst0);
        taxRateRepository.save(gst5);
        taxRateRepository.save(gst12);
        taxRateRepository.save(gst18);
        taxRateRepository.save(gst28);

        // HSN category mappings
        categoryMappingRepository.save(createMapping("map_electronics", "ELECTRONICS", "8471", "gst_18"));
        categoryMappingRepository.save(createMapping("map_clothing", "CLOTHING", "6109", "gst_5"));
        categoryMappingRepository.save(createMapping("map_food", "FOOD", "2106", "gst_12"));
        categoryMappingRepository.save(createMapping("map_luxury", "LUXURY", "7113", "gst_28"));
        categoryMappingRepository.save(createMapping("map_essential", "ESSENTIAL", "0401", "gst_0"));
        categoryMappingRepository.save(createMapping("map_default", "DEFAULT", "0000", "gst_18"));
    }

    private TaxRate createRate(String id, String name, String regionCode, String taxType, BigDecimal rate) {
        TaxRate r = new TaxRate();
        r.setId(id);
        r.setName(name);
        r.setRegionCode(regionCode);
        r.setTaxType(taxType);
        r.setRate(rate);
        r.setEffectiveFrom(LocalDate.of(2017, 7, 1)); // GST effective date
        r.setActive(true);
        return r;
    }

    private TaxCategoryMapping createMapping(String id, String categoryId, String hsnCode, String taxRateId) {
        TaxCategoryMapping m = new TaxCategoryMapping();
        m.setId(id);
        m.setCategoryId(categoryId);
        m.setHsnCode(hsnCode);
        m.setTaxRateId(taxRateId);
        return m;
    }
}

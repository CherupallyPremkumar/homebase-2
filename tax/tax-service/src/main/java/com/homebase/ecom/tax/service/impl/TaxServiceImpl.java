package com.homebase.ecom.tax.service.impl;

import com.homebase.ecom.tax.dto.TaxRateDto;
import com.homebase.ecom.tax.model.OrderTaxLine;
import com.homebase.ecom.tax.model.TaxRate;
import com.homebase.ecom.tax.model.port.OrderTaxLineRepository;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import com.homebase.ecom.tax.service.TaxService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaxServiceImpl implements TaxService {

    private final TaxRateRepository taxRateRepository;
    private final OrderTaxLineRepository orderTaxLineRepository;

    public TaxServiceImpl(TaxRateRepository taxRateRepository, OrderTaxLineRepository orderTaxLineRepository) {
        this.taxRateRepository = taxRateRepository;
        this.orderTaxLineRepository = orderTaxLineRepository;
    }

    @Override
    public List<TaxRateDto> calculateTax(String orderId) {
        List<OrderTaxLine> taxLines = orderTaxLineRepository.findByOrderId(orderId);
        List<TaxRateDto> result = new ArrayList<>();
        for (OrderTaxLine line : taxLines) {
            Optional<TaxRate> rate = taxRateRepository.findById(line.getTaxRateId());
            rate.ifPresent(r -> result.add(toDto(r)));
        }
        return result;
    }

    @Override
    public TaxRateDto getTaxRate(String regionCode, String categoryId) {
        List<TaxRate> rates = taxRateRepository.findByRegionCode(regionCode);
        if (rates.isEmpty()) {
            return null;
        }
        // Return the first active rate for the region
        return rates.stream()
                .filter(TaxRate::isActive)
                .findFirst()
                .map(this::toDto)
                .orElse(null);
    }

    private TaxRateDto toDto(TaxRate model) {
        TaxRateDto dto = new TaxRateDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setRegionCode(model.getRegionCode());
        dto.setTaxType(model.getTaxType());
        dto.setRate(model.getRate());
        if (model.getEffectiveFrom() != null) {
            dto.setEffectiveFrom(java.sql.Date.valueOf(model.getEffectiveFrom()));
        }
        if (model.getEffectiveTo() != null) {
            dto.setEffectiveTo(java.sql.Date.valueOf(model.getEffectiveTo()));
        }
        dto.setActive(model.isActive());
        return dto;
    }
}

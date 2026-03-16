package com.homebase.ecom.tax.configuration.controller;

import com.homebase.ecom.tax.dto.TaxRateDto;
import com.homebase.ecom.tax.model.TaxRate;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import com.homebase.ecom.tax.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tax")
@Tag(name = "Tax", description = "Tax rate calculation")
public class TaxController {

    @Autowired
    private TaxService taxService;

    @Autowired
    private TaxRateRepository taxRateRepository;

    @GetMapping("/rates")
    public ResponseEntity<List<TaxRateDto>> getRatesByRegion(@RequestParam String regionCode) {
        List<TaxRate> rates = taxRateRepository.findByRegionCode(regionCode);
        List<TaxRateDto> dtos = rates.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/rates/{id}")
    public ResponseEntity<TaxRateDto> getRateById(@PathVariable String id) {
        return taxRateRepository.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/calculate/{orderId}")
    public ResponseEntity<List<TaxRateDto>> calculateTax(@PathVariable String orderId) {
        List<TaxRateDto> result = taxService.calculateTax(orderId);
        return ResponseEntity.ok(result);
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

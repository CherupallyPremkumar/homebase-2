package com.homebase.ecom.tax.service.impl;

import com.homebase.ecom.tax.dto.*;
import com.homebase.ecom.tax.model.TaxContext;
import com.homebase.ecom.tax.model.TaxRate;
import com.homebase.ecom.tax.model.OrderTaxLine;
import com.homebase.ecom.tax.model.port.OrderTaxLineRepository;
import com.homebase.ecom.tax.model.port.TaxRateRepository;
import com.homebase.ecom.tax.service.TaxService;
import org.chenile.owiz.OrchExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Tax service implementation using OWIZ pipeline.
 * Stateless: builds TaxContext from request, runs pipeline, maps result to response.
 */
public class TaxServiceImpl implements TaxService {

    private static final Logger log = LoggerFactory.getLogger(TaxServiceImpl.class);

    private final OrchExecutor<TaxContext> taxPipeline;
    private final TaxRateRepository taxRateRepository;
    private final OrderTaxLineRepository orderTaxLineRepository;

    public TaxServiceImpl(OrchExecutor<TaxContext> taxPipeline,
                          TaxRateRepository taxRateRepository,
                          OrderTaxLineRepository orderTaxLineRepository) {
        this.taxPipeline = taxPipeline;
        this.taxRateRepository = taxRateRepository;
        this.orderTaxLineRepository = orderTaxLineRepository;
    }

    @Override
    public TaxCalculationResponseDTO calculateTax(TaxCalculationRequestDTO request) {
        String validationError = validateRequest(request);
        if (validationError != null) {
            return TaxCalculationResponseDTO.error(validationError);
        }

        try {
            TaxContext ctx = buildContext(request);
            taxPipeline.execute(ctx);

            if (ctx.isError()) {
                return TaxCalculationResponseDTO.error(ctx.getErrorMessage());
            }

            return mapToResponse(ctx, request.getCurrency());
        } catch (Exception e) {
            log.error("Tax calculation failed", e);
            return TaxCalculationResponseDTO.error("Tax calculation failed: " + e.getMessage());
        }
    }

    @Override
    public List<TaxRateDto> getOrderTaxLines(String orderId) {
        var taxLines = orderTaxLineRepository.findByOrderId(orderId);
        List<TaxRateDto> result = new ArrayList<>();
        for (OrderTaxLine line : taxLines) {
            TaxRateDto dto = new TaxRateDto();
            dto.setId(line.getId());
            dto.setHsnCode(line.getHsnCode());
            dto.setGstRate(line.getGstRate());
            dto.setActive(true);
            result.add(dto);
        }
        return result;
    }

    @Override
    public TaxRateDto getTaxRate(String regionCode, String categoryId) {
        // Lookup by HSN code (regionCode used as hsnCode for backward compat)
        return taxRateRepository.findActiveByHsnCode(regionCode)
                .map(this::toDto)
                .orElse(null);
    }

    private String validateRequest(TaxCalculationRequestDTO request) {
        if (request == null) return "Request must not be null";
        if (request.getItems() == null || request.getItems().isEmpty()) return "Items must not be empty";
        if (request.getSourceState() == null || request.getSourceState().isBlank()) return "Source state is required";
        if (request.getDestinationState() == null || request.getDestinationState().isBlank()) return "Destination state is required";
        if (request.getCurrency() == null || request.getCurrency().isBlank()) return "Currency is required";

        for (int i = 0; i < request.getItems().size(); i++) {
            TaxableItemDTO item = request.getItems().get(i);
            if (item.getVariantId() == null || item.getVariantId().isBlank())
                return "Item at index " + i + " must have a variantId";
            if (item.getTaxableAmount() <= 0)
                return "Item " + item.getVariantId() + " must have a positive taxable amount";
        }
        return null;
    }

    private TaxContext buildContext(TaxCalculationRequestDTO request) {
        TaxContext ctx = new TaxContext();
        ctx.setSourceState(request.getSourceState());
        ctx.setDestinationState(request.getDestinationState());
        ctx.setCurrency(request.getCurrency());

        List<TaxContext.TaxableItem> items = new ArrayList<>();
        for (TaxableItemDTO dto : request.getItems()) {
            TaxContext.TaxableItem item = new TaxContext.TaxableItem();
            item.setVariantId(dto.getVariantId());
            item.setProductCategory(dto.getProductCategory());
            item.setHsnCode(dto.getHsnCode());
            item.setTaxableAmount(dto.getTaxableAmount());
            item.setQuantity(dto.getQuantity());
            items.add(item);
        }
        ctx.setItems(items);
        return ctx;
    }

    private TaxCalculationResponseDTO mapToResponse(TaxContext ctx, String currency) {
        TaxCalculationResponseDTO response = new TaxCalculationResponseDTO();
        response.setTotalTax(ctx.getTotalTax());
        response.setTaxType(ctx.getTaxType().name());
        response.setCurrency(currency);

        List<TaxLineItemDTO> lineItems = new ArrayList<>();
        for (TaxContext.TaxLineResult result : ctx.getTaxLineResults()) {
            TaxLineItemDTO dto = new TaxLineItemDTO();
            dto.setVariantId(result.getVariantId());
            dto.setHsnCode(result.getHsnCode());
            dto.setTaxableAmount(result.getTaxableAmount());

            if (ctx.getTaxType() == TaxContext.TaxType.INTRA_STATE) {
                BigDecimal halfRate = result.getRate().divide(new BigDecimal("2"), 4, java.math.RoundingMode.HALF_UP);
                dto.setCgstRate(halfRate);
                dto.setCgstAmount(result.getCgstAmount());
                dto.setSgstRate(halfRate);
                dto.setSgstAmount(result.getSgstAmount());
            } else {
                dto.setIgstRate(result.getRate());
                dto.setIgstAmount(result.getIgstAmount());
            }

            if (result.getCessRate() != null && result.getCessRate().compareTo(BigDecimal.ZERO) > 0) {
                dto.setCessRate(result.getCessRate());
                dto.setCessAmount(result.getCessAmount());
            }

            dto.setTotalTax(result.getTotalTax());
            lineItems.add(dto);
        }
        response.setItems(lineItems);

        return response;
    }

    private TaxRateDto toDto(TaxRate model) {
        TaxRateDto dto = new TaxRateDto();
        dto.setId(model.getId());
        dto.setHsnCode(model.getHsnCode());
        dto.setGstRate(model.getGstRate());
        dto.setActive(model.isActive());
        return dto;
    }
}

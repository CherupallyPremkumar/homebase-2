package com.homebase.ecom.promo.service.impl;

import com.homebase.ecom.promo.dto.PricingRequest;
import com.homebase.ecom.promo.dto.PricingResponse;
import com.homebase.ecom.promo.dto.PromotionDto;
import com.homebase.ecom.promo.dto.PromotionResultDto;
import com.homebase.ecom.promo.model.PromoContext;
import com.homebase.ecom.promo.model.Promotion;
import com.homebase.ecom.promo.model.PromotionResult;
import com.homebase.ecom.promo.repository.PromotionRepository;
import com.homebase.ecom.promo.repository.CouponRepository;
import com.homebase.ecom.promo.service.CouponUsageTracker;
import com.homebase.ecom.promo.service.PricingCalculator;
import com.homebase.ecom.promo.service.PromotionService;
import com.homebase.ecom.shared.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PromotionServiceImpl implements PromotionService {

    private static final Logger log = LoggerFactory.getLogger(PromotionServiceImpl.class);

    private final PromotionRepository promotionRepository;
    private final CouponRepository couponRepository;
    private final CouponUsageTracker couponUsageTracker;
    private final PricingCalculator pricingCalculator;

    public PromotionServiceImpl(PromotionRepository promotionRepository,
                                CouponRepository couponRepository,
                                CouponUsageTracker couponUsageTracker,
                                PricingCalculator pricingCalculator) {
        this.promotionRepository = promotionRepository;
        this.couponRepository = couponRepository;
        this.couponUsageTracker = couponUsageTracker;
        this.pricingCalculator = pricingCalculator;
    }

    @Override
    public PricingResponse calculatePrice(PricingRequest request) {
        log.info("Calculating price for coupon: {}", request.getCouponCode());

        PromoContext context = new PromoContext();
        context.setCart(request.getCart());
        context.setCouponCode(request.getCouponCode());

        // 1. Fetch all active promotions
        List<Promotion> activePromotions = promotionRepository.findAllActive();
        context.setEligiblePromotions(activePromotions);

        // 2. If coupon provided, check its validity and add its promotion
        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            Optional<com.homebase.ecom.promo.model.Coupon> couponOpt = couponRepository
                    .findByCode(request.getCouponCode());
            if (couponOpt.isPresent()) {
                com.homebase.ecom.promo.model.Coupon coupon = couponOpt.get();
                if (coupon.isValid()) {
                    // Find the associated promotion
                    promotionRepository.findById(coupon.getId())
                            .ifPresent(context::addEligiblePromotion);
                }
            }
        }

        // 3. Perform calculation
        pricingCalculator.calculate(context);

        // 4. Map results to Response DTO
        return mapToResponse(context);
    }

    @Override
    public boolean validateCoupon(String code) {
        // userId should ideally come from security context, using a dummy UUID for now
        UUID dummyUserId = UUID.randomUUID();
        return couponUsageTracker.canApply(code, dummyUserId);
    }

    private PricingResponse mapToResponse(PromoContext context) {
        PricingResponse response = new PricingResponse();

        if (context.getBestDeal() != null) {
            response.setBestDeal(mapToResultDto(context.getBestDeal()));
            response.setTotalSavings(context.getBestDeal().getSavings());
        }

        if (context.getEvaluationResults() != null) {
            response.setAllApplicablePromotions(context.getEvaluationResults().stream()
                    .filter(PromotionResult::isApplicable)
                    .map(this::mapToResultDto)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private PromotionResultDto mapToResultDto(PromotionResult result) {
        PromotionResultDto dto = new PromotionResultDto();
        dto.setPromotion(mapToPromotionDto(result.getPromotion()));
        dto.setSavings(result.getSavings());
        return dto;
    }

    private PromotionDto mapToPromotionDto(Promotion promotion) {
        PromotionDto dto = new PromotionDto();
        dto.setPromotionId(UUID.fromString(promotion.getId()));
        dto.setName(promotion.getName());
        dto.setDescription(promotion.getDescription());
        return dto;
    }

    public void applyCoupon(String cartId, String couponCode) {
        // Logic to validate and apply coupon
    }

    @Override
    public com.homebase.ecom.promo.dto.PromoApplicationResult applyPromo(
            com.homebase.ecom.promo.dto.PromoCartDTO cart, String promoCode) {
        com.homebase.ecom.promo.dto.PromoApplicationResult result = new com.homebase.ecom.promo.dto.PromoApplicationResult();
        boolean valid = validateCoupon(promoCode);
        result.setValid(valid);
        if (!valid) {
            result.setReasonIfInvalid("Invalid or expired promo code: " + promoCode);
        }
        return result;
    }
}

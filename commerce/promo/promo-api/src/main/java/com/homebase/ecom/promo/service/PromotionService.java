package com.homebase.ecom.promo.service;

import com.homebase.ecom.promo.dto.PricingRequest;
import com.homebase.ecom.promo.dto.PricingResponse;
import com.homebase.ecom.promo.dto.PromoApplicationResult;
import com.homebase.ecom.promo.dto.PromoCartDTO;

public interface PromotionService {
    PricingResponse calculatePrice(PricingRequest request);

    boolean validateCoupon(String code);

    PromoApplicationResult applyPromo(PromoCartDTO cart, String promoCode);
}

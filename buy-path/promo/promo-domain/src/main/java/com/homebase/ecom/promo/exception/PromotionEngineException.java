package com.homebase.ecom.promo.exception;

public class PromotionEngineException extends RuntimeException {
    public PromotionEngineException(String message) {
        super(message);
    }

    public PromotionEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}

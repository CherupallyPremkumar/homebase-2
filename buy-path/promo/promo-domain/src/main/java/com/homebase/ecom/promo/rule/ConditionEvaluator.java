package com.homebase.ecom.promo.rule;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ConditionEvaluator {

    private static final Logger log = LoggerFactory.getLogger(ConditionEvaluator.class);

    public boolean evaluate(Condition condition, CartSnapshot cart) {
        if (condition == null || cart == null) return false;

        switch (condition.getType()) {
            case QUANTITY_THRESHOLD:
                return evaluateQuantityThreshold(condition, cart);
            case AMOUNT_THRESHOLD:
                return evaluateAmountThreshold(condition, cart);
            case CATEGORY_MATCH:
                return evaluateCategoryMatch(condition, cart);
            case REGION_MATCH:
                return evaluateRegionMatch(condition, cart);
            case CUSTOMER_SEGMENT:
                return evaluateCustomerSegment(condition, cart);
            case PRODUCT_ID_MATCH:
                return evaluateProductIdMatch(condition, cart);
            default:
                log.warn("Unsupported condition type: {}", condition.getType());
                return false;
        }
    }

    private boolean evaluateQuantityThreshold(Condition condition, CartSnapshot cart) {
        return applyOperator(cart.getTotalItems(), condition.getOperator(), condition.getValue());
    }

    private boolean evaluateAmountThreshold(Condition condition, CartSnapshot cart) {
        long totalAmount = cart.getTotalAmount().getAmount();
        return applyOperator(totalAmount, condition.getOperator(), condition.getValue());
    }

    private boolean evaluateCategoryMatch(Condition condition, CartSnapshot cart) {
        List<String> cartCategories = cart.getItems().stream()
                .map(item -> item.getCategory())
                .filter(Objects::nonNull)
                .toList();
        return applyOperator(cartCategories, condition.getOperator(), condition.getValue());
    }

    private boolean evaluateRegionMatch(Condition condition, CartSnapshot cart) {
        return applyOperator(cart.getRegion(), condition.getOperator(), condition.getValue());
    }

    private boolean evaluateCustomerSegment(Condition condition, CartSnapshot cart) {
        return applyOperator(cart.getCustomerSegment(), condition.getOperator(), condition.getValue());
    }

    private boolean evaluateProductIdMatch(Condition condition, CartSnapshot cart) {
        List<String> productIds = cart.getItems().stream()
                .map(item -> item.getProductId().toString())
                .toList();
        return applyOperator(productIds, condition.getOperator(), condition.getValue());
    }

    @SuppressWarnings("unchecked")
    private boolean applyOperator(Object actualValue, Operator operator, Object threshold) {
        if (actualValue == null || threshold == null) return false;

        switch (operator) {
            case EQ:
                return Objects.equals(actualValue.toString(), threshold.toString());
            case NOT_EQ:
                return !Objects.equals(actualValue.toString(), threshold.toString());
            case GT:
                return compare(actualValue, threshold) > 0;
            case GTE:
                return compare(actualValue, threshold) >= 0;
            case LT:
                return compare(actualValue, threshold) < 0;
            case LTE:
                return compare(actualValue, threshold) <= 0;
            case IN:
                if (threshold instanceof Collection) {
                    return ((Collection<?>) threshold).contains(actualValue.toString());
                }
                return false;
            case CONTAINS:
                if (actualValue instanceof Collection) {
                    return ((Collection<?>) actualValue).contains(threshold.toString());
                }
                return false;
            default:
                return false;
        }
    }

    private int compare(Object v1, Object v2) {
        try {
            BigDecimal b1 = new BigDecimal(v1.toString());
            BigDecimal b2 = new BigDecimal(v2.toString());
            return b1.compareTo(b2);
        } catch (Exception e) {
            return 0;
        }
    }
}

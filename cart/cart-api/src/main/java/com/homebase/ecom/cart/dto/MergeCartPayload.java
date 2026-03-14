package com.homebase.ecom.cart.dto;

import java.util.List;
import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for merging a guest cart into a user cart.
 */
public class MergeCartPayload extends MinimalPayload {
    public List<MergeItemPayload> items;

    public static class MergeItemPayload {
        public String productId;
        public Integer quantity;
    }
}

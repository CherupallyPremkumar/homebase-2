package com.homebase.ecom.product.infrastructure.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

public class VariantMediaId implements Serializable {
    private String variantId;
    private String assetId;

    public VariantMediaId() {}

    public VariantMediaId(String variantId, String assetId) {
        this.variantId = variantId;
        this.assetId = assetId;
    }

    // hashCode and equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariantMediaId that = (VariantMediaId) o;
        return Objects.equals(variantId, that.variantId) && Objects.equals(assetId, that.assetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variantId, assetId);
    }
}

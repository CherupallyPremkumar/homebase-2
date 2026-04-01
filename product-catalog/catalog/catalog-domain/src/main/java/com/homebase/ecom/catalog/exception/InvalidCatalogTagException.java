package com.homebase.ecom.catalog.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/**
 * Thrown when a tag applied to a CatalogItem is not in the platform's curated
 * tag list.
 */
public class InvalidCatalogTagException extends PolicyViolationException {
    private final String tag;

    public InvalidCatalogTagException(String tag) {
        super("catalog", "Tag '" + tag + "' is not in the platform's approved tag list.");
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}

package com.homebase.ecom.catalog.exception;

import com.homebase.ecom.shared.exception.PolicyViolationException;

/** Thrown when a featured Collection is missing a required image URL. */
public class FeaturedCollectionImageRequiredException extends PolicyViolationException {
    public FeaturedCollectionImageRequiredException() {
        super("catalog",
                "Featured collections must have an image. Please upload an image before marking this collection as featured.");
    }

    public FeaturedCollectionImageRequiredException(String collectionName) {
        super("catalog",
                "Featured collection [" + collectionName + "] must have an image.");
    }
}

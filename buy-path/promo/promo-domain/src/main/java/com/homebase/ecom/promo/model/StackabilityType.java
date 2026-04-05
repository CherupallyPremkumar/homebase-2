package com.homebase.ecom.promo.model;

public enum StackabilityType {
    EXCLUSIVE,        // Only one exclusive can apply
    STACKABLE,        // Multiple can stack together
    MUTUALLY_EXCLUSIVE // Cannot be used with specific other promos
}

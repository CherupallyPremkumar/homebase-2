package com.homebase.ecom.core;

import org.chenile.core.context.HeaderUtils;

import java.util.Map;

public class HmHeaderUtils extends HeaderUtils {

    public static final String CURRENCY_KEY = "x-homebase-currency";

    public static String getCurrency(Map<String, Object> headers) {
        Object val = headers.get(CURRENCY_KEY);
        return val != null ? val.toString() : null;
    }

    public static void setCurrency(Map<String, Object> headers, String currency) {
        headers.put(CURRENCY_KEY, currency);
    }
}

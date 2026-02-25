package com.ecommerce.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.ecommerce.web.controller")
public class WebControllerAdvice {

    @org.springframework.beans.factory.annotation.Value("${app.features.web.refunds:false}")
    private boolean webRefundsEnabled;

    @ModelAttribute("webRefundsEnabled")
    public boolean webRefundsEnabled() {
        return webRefundsEnabled;
    }
}

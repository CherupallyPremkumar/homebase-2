package com.homebase.ecom.configuration;

import com.homebase.ecom.interceptor.CurrencyInterceptor;
import com.homebase.ecom.mapper.CurrencyMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CurrencyProperties.class)
public class CurrencyConfiguration {

    @Bean
    CurrencyInterceptor currencyInterceptor(CurrencyMapper currencyMapper){
        return new CurrencyInterceptor(currencyMapper);
    }

    @Bean
    CurrencyMapper currencyMapper(CurrencyProperties currencyProperties){
        return new CurrencyMapper(currencyProperties);
    }




}

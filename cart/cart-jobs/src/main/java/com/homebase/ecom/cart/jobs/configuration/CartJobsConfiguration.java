package com.homebase.ecom.cart.jobs.configuration;

import com.homebase.ecom.cart.jobs.query.CartQueryAdapter;
import com.homebase.ecom.cart.jobs.query.CartQueryPort;
import com.homebase.ecom.cart.jobs.scheduler.CartAbandonmentJob;
import com.homebase.ecom.cart.jobs.scheduler.CartExpirationJob;
import com.homebase.ecom.cart.jobs.translator.*;
import org.chenile.pubsub.ChenilePub;
import org.chenile.query.service.SearchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class CartJobsConfiguration {

    // --- Query Port Adapter ---

    @Bean
    public CartQueryPort cartQueryPort(
            @Qualifier("cartSearchServiceClient") SearchService cartSearchService) {
        return new CartQueryAdapter(cartSearchService);
    }

    // --- Schedulers ---

    @Bean
    public CartExpirationJob cartExpirationJob(CartQueryPort cartQueryPort, ChenilePub chenilePub) {
        return new CartExpirationJob(cartQueryPort, chenilePub);
    }

    @Bean
    public CartAbandonmentJob cartAbandonmentJob(CartQueryPort cartQueryPort, ChenilePub chenilePub) {
        return new CartAbandonmentJob(cartQueryPort, chenilePub);
    }

    // --- Event Translators ---

    @Bean
    public StockDepletedTranslator stockDepletedTranslator(ChenilePub chenilePub, CartQueryPort cartQueryPort) {
        return new StockDepletedTranslator(chenilePub, cartQueryPort);
    }

    @Bean
    public PriceChangedTranslator priceChangedTranslator(ChenilePub chenilePub, CartQueryPort cartQueryPort) {
        return new PriceChangedTranslator(chenilePub, cartQueryPort);
    }

    @Bean
    public ProductDiscontinuedTranslator productDiscontinuedTranslator(ChenilePub chenilePub, CartQueryPort cartQueryPort) {
        return new ProductDiscontinuedTranslator(chenilePub, cartQueryPort);
    }

    @Bean
    public CouponExpiredTranslator couponExpiredTranslator(ChenilePub chenilePub, CartQueryPort cartQueryPort) {
        return new CouponExpiredTranslator(chenilePub, cartQueryPort);
    }

    @Bean
    public CheckoutCompletedTranslator checkoutCompletedTranslator(ChenilePub chenilePub) {
        return new CheckoutCompletedTranslator(chenilePub);
    }
}

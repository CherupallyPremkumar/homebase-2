package com.homebase.ecom.product.service.event;

import com.homebase.ecom.product.domain.model.Product;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.SupplierProductReturnedEvent;
import org.chenile.query.model.SearchRequest;
import org.chenile.query.model.SearchResponse;
import org.chenile.query.service.SearchService;
import org.chenile.stm.STM;
import org.chenile.utils.entity.service.EntityStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductReturnConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductReturnConsumer.class);

    @Autowired
    @Qualifier("productEntityStm")
    private STM<Product> productStm;

    @Autowired
    @Qualifier("productEntityStore")
    private EntityStore<Product> productEntityStore;

    @Autowired
    private SearchService<Map<String, Object>> searchService;

    @KafkaListener(topics = KafkaTopics.PRODUCT_EVENTS, groupId = "product-service-returns")
    public void consumeProductReturn(SupplierProductReturnedEvent event) {
        log.info("Received SupplierProductReturnedEvent for master product: {}. Reason: {}",
                event.getProductId(), event.getReason());

        try {
            // Fetch the master product using Query Service (Chenile Standard)
            SearchRequest<Map<String, Object>> searchRequest = new SearchRequest<>();
            searchRequest.setQueryName("Product.getById");
            Map<String, Object> filters = new HashMap<>();
            filters.put("id", event.getProductId());
            searchRequest.setFilters(filters);

            SearchResponse response = searchService.doSearch(searchRequest);

            if (response.getList() == null || response.getList().isEmpty()) {
                log.warn("Master product {} not found via Query Service. Cannot discontinue.", event.getProductId());
                return;
            }

            // Load the actual entity for STM transition
            Product product = productEntityStore.retrieve(event.getProductId());
            if (product == null) {
                // This shouldn't happen if SearchService found it, but good to check
                log.warn("Master product {} found in Query Service but failed to load entity.", event.getProductId());
                return;
            }

            // Trigger the 'discontinueProduct' event on the master product aggregate
            // This will move the product to DISCONTINUED state and de-list it from the
            // catalog.
            productStm.proceed(product, "discontinueProduct", null);

            log.info("Master product {} has been discontinued due to return to seller.", event.getProductId());
        } catch (Exception e) {
            log.error("Failed to process SupplierProductReturnedEvent for product: " + event.getProductId(), e);
        }
    }
}

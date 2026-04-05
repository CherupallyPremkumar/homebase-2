package com.homebase.ecom.product.api;

import com.homebase.ecom.product.model.Product;
import org.chenile.base.response.GenericResponse;
import org.chenile.workflow.api.WorkflowRegistry;

public interface ProductService {
    Product create(Product product);
    Product retrieve(String id);
    Product update(String id, Product product);
    void delete(String id);
    Product processById(String id, String eventId, Object payload);
}

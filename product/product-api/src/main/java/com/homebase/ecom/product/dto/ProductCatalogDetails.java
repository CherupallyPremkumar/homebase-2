package com.homebase.ecom.product.dto;

import com.homebase.ecom.shared.Money;
import org.chenile.query.annotation.QueryName;

@QueryName("Product.getById")
public class ProductCatalogDetails {
    private String productId;
    private String name;
    private String category;
    private Money price;
    private Boolean active;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Money getPrice() { return price; }
    public void setPrice(Money price) { this.price = price; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

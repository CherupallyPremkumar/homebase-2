package com.homebase.ecom.catalog.domain.port.out;

import java.util.List;

public interface ProductClientPort {
    ProductOverview fetchProductOverview(String productId);
    
    class ProductOverview {
        private String id;
        private String name;
        private String slug;
        private List<String> categoryIds;
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public List<String> getCategoryIds() { return categoryIds; }
        public void setCategoryIds(List<String> categoryIds) { this.categoryIds = categoryIds; }
    }
}

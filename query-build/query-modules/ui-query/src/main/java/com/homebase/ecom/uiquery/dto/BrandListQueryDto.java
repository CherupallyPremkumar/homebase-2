package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;

public class BrandListQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private long productCount;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public long getProductCount() { return productCount; }
    public void setProductCount(long productCount) { this.productCount = productCount; }
}

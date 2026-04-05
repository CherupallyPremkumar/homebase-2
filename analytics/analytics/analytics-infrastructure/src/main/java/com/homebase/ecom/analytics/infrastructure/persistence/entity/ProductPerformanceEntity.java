package com.homebase.ecom.analytics.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "product_performance")
public class ProductPerformanceEntity extends BaseJpaEntity {

    @Column(name = "product_id")
    private String productId;

    @Column(name = "period_date")
    private LocalDate periodDate;

    @Column(name = "views")
    private int views;

    @Column(name = "add_to_cart_count")
    private int addToCartCount;

    @Column(name = "purchases")
    private int purchases;

    @Column(name = "units_sold")
    private int unitsSold;

    @Column(name = "revenue")
    private BigDecimal revenue;

    @Column(name = "returns_count")
    private int returnsCount;

    @Column(name = "avg_rating")
    private BigDecimal avgRating;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public LocalDate getPeriodDate() { return periodDate; }
    public void setPeriodDate(LocalDate periodDate) { this.periodDate = periodDate; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getAddToCartCount() { return addToCartCount; }
    public void setAddToCartCount(int addToCartCount) { this.addToCartCount = addToCartCount; }

    public int getPurchases() { return purchases; }
    public void setPurchases(int purchases) { this.purchases = purchases; }

    public int getUnitsSold() { return unitsSold; }
    public void setUnitsSold(int unitsSold) { this.unitsSold = unitsSold; }

    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }

    public int getReturnsCount() { return returnsCount; }
    public void setReturnsCount(int returnsCount) { this.returnsCount = returnsCount; }

    public BigDecimal getAvgRating() { return avgRating; }
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }
}

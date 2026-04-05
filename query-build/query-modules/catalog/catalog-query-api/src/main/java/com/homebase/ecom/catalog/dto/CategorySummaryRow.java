package com.homebase.ecom.catalog.dto;

import java.io.Serializable;

public class CategorySummaryRow implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalCategories;
    private int activeCategories;
    private int inactiveCategories;
    private int rootCount;
    private int subCount;
    private int leafCount;

    public int getTotalCategories() { return totalCategories; }
    public void setTotalCategories(int totalCategories) { this.totalCategories = totalCategories; }
    public int getActiveCategories() { return activeCategories; }
    public void setActiveCategories(int activeCategories) { this.activeCategories = activeCategories; }
    public int getInactiveCategories() { return inactiveCategories; }
    public void setInactiveCategories(int inactiveCategories) { this.inactiveCategories = inactiveCategories; }
    public int getRootCount() { return rootCount; }
    public void setRootCount(int rootCount) { this.rootCount = rootCount; }
    public int getSubCount() { return subCount; }
    public void setSubCount(int subCount) { this.subCount = subCount; }
    public int getLeafCount() { return leafCount; }
    public void setLeafCount(int leafCount) { this.leafCount = leafCount; }
}

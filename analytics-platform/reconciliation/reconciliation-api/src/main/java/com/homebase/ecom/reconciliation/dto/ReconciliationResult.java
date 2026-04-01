package com.homebase.ecom.reconciliation.dto;

import java.util.ArrayList;
import java.util.List;

public class ReconciliationResult {
    private int totalTransactions;
    private int matched;
    private int mismatched;
    private int autoResolved;
    private int pendingReview;
    private List<MismatchDto> mismatches = new ArrayList<>();

    public ReconciliationResult() {
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public int getMatched() {
        return matched;
    }

    public void setMatched(int matched) {
        this.matched = matched;
    }

    public int getMismatched() {
        return mismatched;
    }

    public void setMismatched(int mismatched) {
        this.mismatched = mismatched;
    }

    public int getAutoResolved() {
        return autoResolved;
    }

    public void setAutoResolved(int autoResolved) {
        this.autoResolved = autoResolved;
    }

    public int getPendingReview() {
        return pendingReview;
    }

    public void setPendingReview(int pendingReview) {
        this.pendingReview = pendingReview;
    }

    public List<MismatchDto> getMismatches() {
        return mismatches;
    }

    public void setMismatches(List<MismatchDto> mismatches) {
        this.mismatches = mismatches;
    }
}

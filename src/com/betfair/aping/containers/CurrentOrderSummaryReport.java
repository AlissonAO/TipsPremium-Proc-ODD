package com.betfair.aping.containers;

import java.util.List;

import com.betfair.aping.entities.CurrentOrderSummary;

public class CurrentOrderSummaryReport extends Container {
    public List<CurrentOrderSummary> getCurrentOrders() {
        return currentOrders;
    }

    public void setCurrentOrders(List<CurrentOrderSummary> currentOrders) {
        this.currentOrders = currentOrders;
    }

    public boolean isMoreAvailable() {
        return moreAvailable;
    }

    public void setMoreAvailable(boolean moreAvailable) {
        this.moreAvailable = moreAvailable;
    }

    private List<CurrentOrderSummary> currentOrders;
    private boolean moreAvailable;
}
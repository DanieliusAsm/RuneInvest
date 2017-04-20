package com.example.danielius.runeinvest.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Danielius on 23-Aug-15.
 */
public class Price {

    @SerializedName("trend")
    private String itemTrend;

    @SerializedName("price")
    private String itemPrice;

    public String getItemTrend() {
        return itemTrend;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemTrend(String itemTrend) {
        this.itemTrend = itemTrend;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}


package com.example.danielius.runeinvest.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Danielius on 23-Aug-15.
 */
public class Item {

    @SerializedName("icon_large")
    private String icon;

    @SerializedName("id")
    private String itemId;

    @SerializedName("name")
    private String itemName;

    @SerializedName("description")
    private String itemDescription;

    @SerializedName("current")
    private Price currentPrice;

    @SerializedName("today")
    private Price priceChange;


    public String getIcon() {
        return icon;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Price getCurrentPrice() {
        return currentPrice;
    }

    public Price getPriceChange() {
        return priceChange;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setCurrentPrice(Price currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setPriceChange(Price priceChange) {
        this.priceChange = priceChange;
    }
}

package com.example.danielius.runeinvest.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Danielius on 25-Aug-15.
 */
public class Alpha {

    @SerializedName("letter")
    private String letter;

    @SerializedName("items")
    private String items;

    public String getLetter() {
        return letter;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}

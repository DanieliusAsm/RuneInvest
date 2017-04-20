package com.example.danielius.runeinvest.api.response;

import com.example.danielius.runeinvest.api.model.Item;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Danielius on 23-Aug-15.
 */
public class ItemResponse {

    @SerializedName("items")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }
}

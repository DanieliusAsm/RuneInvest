package com.example.danielius.runeinvest.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Danielius on 23-Aug-15.
 */
public class GraphResponse {

    @SerializedName("daily")
    private Map<String, String> data;

    public Map<String, String> getData() {
        return data;
    }
}

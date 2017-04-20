package com.example.danielius.runeinvest.api.response;

import com.example.danielius.runeinvest.api.model.Alpha;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Danielius on 23-Aug-15.
 */
public class CategoryResponse {

    @SerializedName("alpha")
    private List<Alpha> alpha;

    public List<Alpha> getAlpha() {
        return alpha;
    }
}

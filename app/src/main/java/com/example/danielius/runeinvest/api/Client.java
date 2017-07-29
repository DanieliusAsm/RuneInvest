package com.example.danielius.runeinvest.api;

import android.util.Log;

import com.example.danielius.runeinvest.api.response.CategoryResponse;
import com.example.danielius.runeinvest.api.response.GraphResponse;
import com.example.danielius.runeinvest.api.response.ItemInfoResponse;
import com.example.danielius.runeinvest.api.response.ItemResponse;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * OkHttp API Client for Android application.
 *
 * Depends on:
 * com.squareup.retrofit:retrofit
 * com.squareup.okhttp:okhttp
 * com.squareup.okhttp:okhttp-urlconnection
 * com.pixplicity.easyprefs:library
 */
public class Client {

    /**
     * Public Client instance.
     */
    private static API _instance;

    /**
     * Client url.
     */
    private static final String API_URL = "http://services.runescape.com/m=itemdb_rs/api";

    /**
     * Preferences key for saving cookies.
     */
    private static final String PREF_COOKIES = "cookies";

    public interface API {

        // category information
        @GET("/catalogue/category.json")
        void getCategory(@Query("category") String category,Callback<CategoryResponse> callback);

        // get all items in this category
        @GET("/catalogue/items.json")
        void getItemsInCategory(@Query("category") String category,@Query("alpha") String alpha,@Query("page") String page,Callback<ItemResponse> callback);

        @GET("/graph/{item_id}.json")
        void getItemGraph(@Path("item_id") String itemId,Callback<GraphResponse> callback);

    }

    /**
     * Create or get an instance of Client client.
     *
     * @return Client
     */
    public static API get() {
        if (_instance != null) {
            return _instance;
        }
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(20000, TimeUnit.MILLISECONDS);


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setClient(new OkClient(client))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        _instance = restAdapter.create(API.class);

        return _instance;
    }


}
package com.example.btvn.ApiService;

import com.example.btvn.models.Item;
import com.example.btvn.models.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewFeedApi {

    @GET("newsfeed.json")
    Call<Root> getListNewFeed();
}

package com.example.appbansach.api;

import com.example.appbansach.model.Publisher;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PublisherApi {
    @GET("publisher")
    Call<Publisher> getAllPublisher();

}

package com.example.appbansach.api;

import com.example.appbansach.model.Rate;
import com.example.appbansach.model.RateDto;
import com.example.appbansach.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RateApi {
    @POST("rate")
    Call<Rate> createRate(
            @Body RateDto rateDto
    );

    @GET("rate")
    Call<List<Rate>> getRateByBookId(@Query("bookId") Integer bookId);
}

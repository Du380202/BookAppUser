package com.example.appbansach.api;

import com.example.appbansach.model.Book;
import com.example.appbansach.model.Cart;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CartApi {

    @GET("cart")
    Call<List<Cart>> getCart(
            @Query("userId") int userId
    );

    @POST("cart/add")
    Call<ResponseBody> addCart(
            @Query("bookId") int bookId,
            @Query("userId") int userId
    );
    @POST("cart/remove")
    Call<Void> removeCart(
            @Query("cartId") int cartId
    );

}

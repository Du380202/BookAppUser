package com.example.appbansach.api;

import com.example.appbansach.model.Category;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CategoryApi {
    @GET("category")
    Call<List<Category>> getAllCategory();
//
//    @Multipart
//    @POST("category")
//    Call<String> createCategory(@Part MultipartBody.Part image,
//                                @Part("categoryDto") RequestBody categoryDto);

//    @Multipart
//    @PUT("category")
//    Call<String> updateCategory(@Part MultipartBody.Part image,
//                                @Part("categoryDto") RequestBody categoryDto);

//    @DELETE("category/{id}")
//    Call<ApiResponse> deleteCategory(@Path("id") int id);
}

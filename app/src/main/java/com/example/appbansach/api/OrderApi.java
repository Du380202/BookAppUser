package com.example.appbansach.api;

import com.example.appbansach.model.ApiResponse;
import com.example.appbansach.model.ApiResponseObject;
import com.example.appbansach.model.RequestOrderModel;
import com.example.appbansach.model.ResponseHistoryOrderModel;
import com.example.appbansach.model.ResponseOrderModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface OrderApi {

    @POST("order")
    Call<ApiResponseObject>createOrders(
            @Body RequestOrderModel requestOrderModel
            );

    @GET("order")
    Call<List<ResponseHistoryOrderModel>>getHistoryOrders(
            @Query("userId") int userId
    );

    @POST("order")
    Call<List<ResponseHistoryOrderModel>>getOrdersStatus(
            @Query("status") int status
    );

    @POST("order/cancel")
    Call<ApiResponse>cancelOrder(
            @Query("orderId") int orderId
    );

    @PUT("order/update")
    Call<ApiResponse>successOrder(
            @Query("orderId") int orderId, @Query("statusNumber") int statusNumber
    );

    @POST("order/payment")
    Call<ApiResponse> updateToken(@Query("orderId") Integer orderId, @Query("token") String token);




}

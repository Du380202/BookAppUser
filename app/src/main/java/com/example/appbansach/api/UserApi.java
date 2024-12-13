package com.example.appbansach.api;

import com.example.appbansach.model.Book;
import com.example.appbansach.model.Cart;
import com.example.appbansach.model.LoginUserModel;
import com.example.appbansach.model.RequestOrderModel;
import com.example.appbansach.model.ResponseChangePass;
import com.example.appbansach.model.ResponseOrderModel;
import com.example.appbansach.model.User;
import com.example.appbansach.model.UserEditResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserApi {

    @POST("register")
    Call<User> registerUser(
            @Body User user
    );

    @POST("login")
    Call<User> loginUser(
           @Body LoginUserModel loginUserModel
            );
    // cap nhat thong tin ca nhan
    @PUT("user")
    Call<User>updateUser(
            @Body UserEditResponse userEditResponse
            );

    @PUT("user/password")
    Call<User>changePassUser(
            @Body ResponseChangePass responseChangePass
            );
}

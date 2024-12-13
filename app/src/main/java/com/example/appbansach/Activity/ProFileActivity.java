package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appbansach.R;
import com.example.appbansach.api.UserApi;
import com.example.appbansach.model.User;
import com.example.appbansach.model.UserEditResponse;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProFileActivity extends AppCompatActivity {
    EditText fullnamePro, emailPro, sdtPro, addressPro;
    AppCompatButton editPro, changePro;
    AppCompatButton btnLogout;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_file);
        setControl();
        setEvent();

    }

    private void setControl() {
        fullnamePro = findViewById(R.id.fullNameprofile);
        emailPro = findViewById(R.id.emailprofile);
        sdtPro = findViewById(R.id.mobileprofile);
        addressPro = findViewById(R.id.addressprofile);
        editPro = findViewById(R.id.editProfile);
        changePro = findViewById(R.id.changePassProfile);
        btnLogout = findViewById(R.id.logoutProfile);
        backIcon = findViewById(R.id.backIcon);

        // luc nhan dang nhap thi no tu luu vao paper roi
        if(Paper.book().read("email") != null && Paper.book().read("password") != null){
            fullnamePro.setText(Utils.user_current.getFullName());
            emailPro.setText(Utils.user_current.getEmail());
            sdtPro.setText(Utils.user_current.getPhoneNumber());
            addressPro.setText(Utils.user_current.getAddress());
        }
    }

    private void setEvent() {
        changePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changepass = new Intent(getApplicationContext(), ChangePassWord.class);
                startActivity(changepass);
            }
        });

        editPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_fullname = fullnamePro.getText().toString().trim();
                String str_mobile = sdtPro.getText().toString().trim();
                String str_email = emailPro.getText().toString().trim();
                String str_address = addressPro.getText().toString().trim();

                if(TextUtils.isEmpty(str_fullname)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập full name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_mobile)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập mobile", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_address)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else{
                    RetrofitClient retrofitClient = new RetrofitClient();
                    UserApi userApi = retrofitClient.getRetrofit().create(UserApi.class);
                    UserEditResponse userEditResponse = new UserEditResponse();
                    userEditResponse.setUserId(Utils.user_current.getUserId());
                    userEditResponse .setAddress(str_address);
                    userEditResponse .setEmail(str_email);
                    userEditResponse .setPhoneNumber(str_mobile);
                    userEditResponse .setFullName(str_fullname);
                    userApi.updateUser(userEditResponse).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Cập nhật thông tin cá nhân thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(), "Cập nhật thông tin cá nhân thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProFileActivity.this, LoginActivity.class));
            }
        });


    }
}
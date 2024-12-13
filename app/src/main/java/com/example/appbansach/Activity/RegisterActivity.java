package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.R;
import com.example.appbansach.api.UserApi;
import com.example.appbansach.model.User;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean conPasswordShowing = false;
    private EditText fullname, username, email, mobile, password, conPassword,address;
    private ImageView conPasswordIcon,passwordIcon;

    private AppCompatButton signUpBtn;
    private TextView signInBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setControl();
        setEvent();
    }

    private void setEvent() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_fullname = fullname.getText().toString().trim();
                String str_username = username.getText().toString().trim();
                String str_email = email.getText().toString().trim();
                String str_pass = password.getText().toString().trim();
                String str_address = address.getText().toString().trim();
                String str_mobile = mobile.getText().toString().trim();
                String str_conpass = conPassword.getText().toString().trim();
                User user = new User();
                user.setFullName(str_fullname);
                user.setUsername(str_username);
                user.setEmail(str_email);
                user.setPassword(str_pass);
                user.setRetryPassword(str_conpass);
                user.setAddress(str_address);
                user.setPhoneNumber(str_mobile);

                if(TextUtils.isEmpty(str_fullname)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Full name",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập mật khẩu",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_conpass)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập xác nhận mật khẩu", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_mobile)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập số Mobile", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_address)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_username)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập user name", Toast.LENGTH_SHORT).show();
                }else {
                    if(str_conpass.equals(str_pass)){
                        RetrofitClient retrofitClient = new RetrofitClient();
                        UserApi userApi = retrofitClient.getRetrofit().create(UserApi.class);
                        userApi.registerUser(user).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.isSuccessful()){
//                                    Utils.user_current.setEmail(str_email);
                                    Utils.user_current.setUsername(str_username);
                                    Utils.user_current.setPassword(str_pass);
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Đăng kí thất baị",Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(),"Password chưa khớp",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // visible hien thi, variation an
        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing) {
                    passwordShowing = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hienthi_mk);
                } else {
                    passwordShowing = true;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.an_mk);
                }
                // move the cursor at last of the text
                password.setSelection(password.length());
            }
        });
        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conPasswordShowing) {
                    conPasswordShowing = false;
                    conPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.hienthi_mk);
                } else {
                    conPasswordShowing = true;
                    conPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.an_mk);
                }
                // move the cursor at last of the text
                conPassword.setSelection(conPassword.length());
            }
        });

    }

    private void setControl() {
        fullname = findViewById(R.id.fullNameET);
        username = findViewById(R.id.userNameET);
        address = findViewById(R.id.addressET);
        email = findViewById(R.id.emailET);
        mobile = findViewById(R.id.mobileET);

        password = findViewById(R.id.passwordET);
        conPassword = findViewById(R.id.conPasswordET);
        passwordIcon = findViewById(R.id.passwordIcon);
        conPasswordIcon = findViewById(R.id.conPasswordIcon);

        signUpBtn = findViewById(R.id.signUpBtn);
        signInBtn = findViewById(R.id.signInBtn);

    }

}
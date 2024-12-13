package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.R;
import com.example.appbansach.api.UserApi;
import com.example.appbansach.model.LoginUserModel;
import com.example.appbansach.model.User;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText emailET, passwordET;
    private ImageView passwordIcon;
    private AppCompatButton signInBtn;

    private TextView forgotPasswordBtn, signUpBtn;
    private boolean passwordShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
    }

    private void setEvent() {
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = emailET.getText().toString().trim();
                String str_pass = passwordET.getText().toString().trim();

//                Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_SHORT).show();
                // Log the username and password to verify the values
                Log.d("LoginActivity", "Username: " + str_email);
                Log.d("LoginActivity", "Password: " + str_pass);
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập tài khoản hoặc mật khẩu",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập mật khẩu",Toast.LENGTH_SHORT).show();
                }else {

                    // save information login, mỗi khi out ứng dụng nó tự lưu
                    Paper.book().write("email",str_email);
                    Paper.book().write("password",str_pass);

                    LoginUserModel loginUserModel = new LoginUserModel();
                    loginUserModel.setUsername(str_email);
                    loginUserModel.setPassword(str_pass);

                    Log.d("LoginActivity", "LoginUserModel created with Username: " + loginUserModel.getUsername() + " and Password: " + loginUserModel.getPassword());
                    RetrofitClient retrofitClient = new RetrofitClient();
                    UserApi userApi = retrofitClient.getRetrofit().create(UserApi.class);
                    userApi.loginUser(loginUserModel).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                Log.d("LoginActivity", "LoginUserModel created with Username: " + loginUserModel.getUsername() + " and Password: " + loginUserModel.getPassword());

                                Utils.user_current = response.body();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"Tài khoản hoặc mật khẩu không đúng",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("LogError", "LoginUserModel created with Username: " + t.getMessage() + " and Password: " + loginUserModel.getPassword());

                        }
                    });
                }

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if password showing or not
                if (passwordShowing) {
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hienthi_mk);
                } else {
                    passwordShowing = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.an_mk);
                }
                // move the cursor at last of the text
                passwordET.setSelection(passwordET.length());
            }
        });
    }

    private void setControl() {
        Paper.init(this);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        passwordIcon = findViewById(R.id.passwordIcon);
        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);

        // read data login sau khi thoát ứng dụng
        if(Paper.book().read("email") != null && Paper.book().read("password") != null){
            emailET.setText(Paper.book().read("email"));
            passwordET.setText(Paper.book().read("password"));
        }
    }
    // set email pass, tu dang nhap bang utils bien cuc bo
    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail() != null && Utils.user_current.getPassword() != null){
            emailET.setText(Utils.user_current.getUsername());
            passwordET.setText(Utils.user_current.getPassword());
        }
    }
}
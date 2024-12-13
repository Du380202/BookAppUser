package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbansach.R;
import com.example.appbansach.api.UserApi;
import com.example.appbansach.model.LoginUserModel;
import com.example.appbansach.model.ResponseChangePass;
import com.example.appbansach.model.User;
import com.example.appbansach.model.UserEditResponse;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassWord extends AppCompatActivity {
    EditText passnew, passold, confirmpass;
    AppCompatButton changePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        setControl();
        setEvent();
    }

    private void setEvent() {
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_passold = passold.getText().toString().trim();
                String str_passnew = passnew.getText().toString().trim();
                String str_confirmpass = confirmpass.getText().toString().trim();
                if(TextUtils.isEmpty(str_passold)){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_passnew)){
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập mật khẩu mới",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_confirmpass)){
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập xác nhận mật khẩu",Toast.LENGTH_SHORT).show();
                }else{
                    if(str_passnew.equals(str_confirmpass)){
//                        compositeDisposable.add(apiBanGiay.changePass(Utils.user_current.getId(),str_passold,str_passnew)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(
//                                        // do no trả về succes, message thôi nên tận dụng giohangmodel nó cũng trả về success, message
//                                        gioHangModel -> {
//                                            if(gioHangModel.getStatus() == 200){
//                                                Toast.makeText(getApplicationContext(),"Thay đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
//                                                startActivity(intent);
//                                            }else{
//                                                Toast.makeText(getApplicationContext(),"Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
//                                            }
//                                        },
//                                        throwable -> {
//                                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
//                                        }
//                                ));
                        RetrofitClient retrofitClient = new RetrofitClient();
                        UserApi userApi = retrofitClient.getRetrofit().create(UserApi.class);
                        ResponseChangePass responseChangePass = new ResponseChangePass();
                        responseChangePass.setUserId(Utils.user_current.getUserId());
                        responseChangePass.setPassword(str_passold);
                        responseChangePass.setNewPassword(str_passnew);
                        responseChangePass.setRetryPassword(str_confirmpass);
                        userApi.changePassUser(responseChangePass).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu  thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(),"Password chưa khớp",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void setControl() {
        passnew = findViewById(R.id.passwordNew);
        passold = findViewById(R.id.passwordOld);
        confirmpass = findViewById(R.id.confirmpass);
        changePass = findViewById(R.id.changePass);
    }
}
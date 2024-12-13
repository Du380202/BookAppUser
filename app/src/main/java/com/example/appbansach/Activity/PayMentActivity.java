package com.example.appbansach.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.R;
import com.example.appbansach.api.CreateOrder;
import com.example.appbansach.api.OrderApi;
import com.example.appbansach.model.ApiResponse;
import com.example.appbansach.model.ApiResponseObject;
import com.example.appbansach.model.Order;
import com.example.appbansach.model.RequestOrderModel;
import com.example.appbansach.model.ResponseOrderModel;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.DialogCallBack;
import com.example.appbansach.utils.DialogUtils;
import com.example.appbansach.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PayMentActivity extends AppCompatActivity {
    TextView txtTongtien, txtAdress, tvEmail, tvPhone, tvFullName, tvSelectedPaymentMethod;
    ImageView backIcon;
    BigDecimal totalPrice;
    Button btnOrder, btnPutAddress;
    LinearLayout zaloPayOption, cashOnDeliveryOption,  momoOption;
    int selectedMethod;
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "Thanh toán hóa đơn";
    private String merchantCode = "Momo19072017";
    private String merchantNameLabel = "Dịch vụ";
    private String description = "Thanh toán hóa đơn";
    private String token;
    Order order = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ment);
        setControl();
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(2553, Environment.SANDBOX);

        zaloPayOption.setOnClickListener(v -> {
            highlightSelectedMethod(zaloPayOption);
            selectedMethod = 1;
            tvSelectedPaymentMethod.setText("ZaloPay");
        });

        momoOption.setOnClickListener(v -> {
            highlightSelectedMethod(momoOption);
            selectedMethod = 2;
            tvSelectedPaymentMethod.setText("Momo");
        });
//
        cashOnDeliveryOption.setOnClickListener(v -> {
            highlightSelectedMethod(cashOnDeliveryOption);
            selectedMethod = 3;
            tvSelectedPaymentMethod.setText("Cash on Delivery");
        });
        btnPutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeliveryInfoDialog();
            }
        });
        initControl();
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

    }

    private RequestOrderModel getDataFromLayout() {
        RequestOrderModel requestOrderModel = new RequestOrderModel();
        requestOrderModel.setAddress(txtAdress.getText().toString());
        requestOrderModel.setEmail(tvEmail.getText().toString());
        requestOrderModel.setFullName(tvFullName.getText().toString());
        requestOrderModel.setStatus(0);
        requestOrderModel.setPhoneNumber(tvPhone.getText().toString());
        requestOrderModel.setUserId(Utils.user_current.getUserId());
        return  requestOrderModel;
    }

    private void initControl() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Intent intent = getIntent();
        String tongTienString = intent.getStringExtra("totalPrice");
        assert tongTienString != null;
        if (tongTienString.endsWith(".00")) {
            tongTienString = tongTienString.substring(0, tongTienString.length() - 3);
        }
        Log.d("tongTienString", tongTienString);
        BigDecimal totalPrice = new BigDecimal(tongTienString);
        Log.d("tongTien", String.valueOf(totalPrice));
        txtTongtien.setText(decimalFormat.format(totalPrice));
        txtAdress.setText(Utils.user_current.getAddress());
        tvEmail.setText(Utils.user_current.getEmail());
        tvFullName.setText(Utils.user_current.getFullName());
        txtAdress.setText(Utils.user_current.getAddress());
        tvPhone.setText(Utils.user_current.getPhoneNumber());
        RequestOrderModel requestOrderModel = getDataFromLayout();
            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedMethod == 3) {
                        cashOption(requestOrderModel);

                    } else if(selectedMethod == 2) {
                        requestPayment(String.valueOf(totalPrice));
//                        Log.d("Mylog", token);
                    }
                }
            });



    }
    private void cashOption(RequestOrderModel requestOrderModel) {
        RetrofitClient retrofitClient = new RetrofitClient();
        OrderApi orderApi = retrofitClient.getRetrofit().create(OrderApi.class);
        orderApi.createOrders(requestOrderModel).enqueue(new Callback<ApiResponseObject>() {
            @Override
            public void onResponse(Call<ApiResponseObject> call, Response<ApiResponseObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponseObject apiResponse = response.body();
                    order = apiResponse.getOrder();
                    Log.d("MyLog", order.toString());
                    DialogUtils.showSuccessDialog(PayMentActivity.this, apiResponse.getMessage(), new DialogCallBack() {
                        @Override
                        public void onConfirm() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ApiResponseObject errorResponse = gson.fromJson(response.errorBody().string(), ApiResponseObject.class);
                            DialogUtils.showErrorDialog(PayMentActivity.this, errorResponse.getMessage());
                        } else {
                            Log.e("Error", "HTTP Status: " + response.code() + ", Message: Không có nội dung lỗi");
                        }
                    } catch (IOException e) {
                        Log.e("Error", e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseObject> call, Throwable t) {
                Log.e("LogPayment", t.getMessage());
            }
        });
    }
    // Momo
    private void createOrderWithPayment(RequestOrderModel requestOrderModel) {
        RetrofitClient retrofitClient = new RetrofitClient();
        OrderApi orderApi = retrofitClient.getRetrofit().create(OrderApi.class);
        orderApi.createOrders(requestOrderModel).enqueue(new Callback<ApiResponseObject>() {
            @Override
            public void onResponse(Call<ApiResponseObject> call, Response<ApiResponseObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponseObject apiResponse = response.body();
                    order = apiResponse.getOrder();
                    updateToken(token, order.getOrderId());
                    DialogUtils.showSuccessDialog(PayMentActivity.this, "Thanh toán thành công", new DialogCallBack() {
                        @Override
                        public void onConfirm() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ApiResponseObject errorResponse = gson.fromJson(response.errorBody().string(), ApiResponseObject.class);
                            DialogUtils.showErrorDialog(PayMentActivity.this, errorResponse.getMessage());
                        } else {
                            Log.e("Error", "HTTP Status: " + response.code() + ", Message: Không có nội dung lỗi");
                        }
                    } catch (IOException e) {
                        Log.e("Error", e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseObject> call, Throwable t) {
                Log.e("LogPayment", t.getMessage());
            }
        });
    }
    //Momo
    private void requestPayment(String totalPrice) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME, merchantName);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_CODE, merchantCode);
        eventValue.put(MoMoParameterNamePayment.AMOUNT, totalPrice);
        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description);
        //client Optional
        eventValue.put(MoMoParameterNamePayment.FEE, fee);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME_LABEL, merchantNameLabel);

        eventValue.put(MoMoParameterNamePayment.REQUEST_ID,  merchantCode+"-"+ UUID.randomUUID().toString());
        eventValue.put(MoMoParameterNamePayment.PARTNER_CODE, "CGV19072017");
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put(MoMoParameterNamePayment.EXTRA_DATA, objExtraData.toString());
        eventValue.put(MoMoParameterNamePayment.REQUEST_TYPE, "payment");
        eventValue.put(MoMoParameterNamePayment.LANGUAGE, "vi");
        eventValue.put(MoMoParameterNamePayment.EXTRA, "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RequestOrderModel requestOrderModel = getDataFromLayout();
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    Log.d("Mylog", "Get token " + data.getStringExtra("message"));
                    token = data.getStringExtra("data");
                    createOrderWithPayment(requestOrderModel);
                    Log.d("MyLog", token);
                    if(data.getStringExtra("data") != null && !data.getStringExtra("data").equals("")) {
                        // TODO:
                    } else {
                        Log.d("Message", "message: " + "Lỗi");                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.d("Message", "message: " + "Lỗi");                } else if(data.getIntExtra("status", -1) == 2) {
                    Log.d("Message", "message: " + "Lỗi");                } else {
                    Log.d("Message", "message: " + "Lỗi");                }
            } else {
                Log.d("Message", "message: " + "Lỗi");            }
        } else {
            Log.d("Message", "message: " + "Lỗi");        }
    }
    //Momo
    private void zaloPay() {
        CreateOrder orderApi = new CreateOrder();
        RequestOrderModel requestOrderModel = getDataFromLayout();
        try {
            JSONObject data = orderApi.createOrder("10000");
            String code = data.getString("return_code");

            if (code.equals("1")) {
                token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(PayMentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        createOrderWithPayment(requestOrderModel); // xử lý thanh toán
                        Intent intent1 = new Intent(PayMentActivity.this, PaymentNotify.class);
                        intent1.putExtra("result", "Thanh toan thanh cong");
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Intent intent1 = new Intent(PayMentActivity.this, PaymentNotify.class);
                        intent1.putExtra("result", "Huy");
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Intent intent1 = new Intent(PayMentActivity.this, PaymentNotify.class);
                        intent1.putExtra("result", "Thanh toan that bai");
                    }
                });
            }

        } catch (Exception e) {
            Log.d("LogZalo", e.getMessage());
        }
    }

    private void updateToken(String token, Integer orderId) {
        RetrofitClient retrofitClient = new RetrofitClient();
        OrderApi orderApi = retrofitClient.getRetrofit().create(OrderApi.class);
        orderApi.updateToken(orderId, token).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Success ", response.body().getStatus()+"");
                } else {
                    Log.d("Error ", response.body().getStatus()+"");
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("LogPay", t.getMessage());
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }


    private void highlightSelectedMethod(LinearLayout selectedOption) {
        zaloPayOption.setBackgroundColor(Color.WHITE);
        cashOnDeliveryOption.setBackgroundColor(Color.WHITE);

        selectedOption.setBackgroundColor(Color.LTGRAY);
    }


    private void setControl() {
        txtTongtien = findViewById(R.id.tvTotalAmount);
        txtAdress = findViewById(R.id.tvDeliveryAddress);
        btnOrder = findViewById(R.id.btnProceedPayment);
        backIcon = findViewById(R.id.backIcon);
        zaloPayOption = findViewById(R.id.zaloPayOption);
        cashOnDeliveryOption = findViewById(R.id.cashOnDeliveryOption);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        btnPutAddress = findViewById(R.id.btnPutAddress);
        tvSelectedPaymentMethod = findViewById(R.id.tvSelectedPaymentMethod);
        momoOption = findViewById(R.id.momoOption);
    }

    private void showDeliveryInfoDialog() {
        Dialog dialog = new Dialog(PayMentActivity.this);
        dialog.setContentView(R.layout.dialog_delivery_address); // Layout của dialog

        EditText etRecipientName = dialog.findViewById(R.id.etRecipientName);
        EditText etEmail = dialog.findViewById(R.id.etEmail);
        EditText etPhone = dialog.findViewById(R.id.etPhone);
        EditText etDeliveryAddress = dialog.findViewById(R.id.etDeliveryAddress);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Đóng dialog
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipientName = etRecipientName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String deliveryAddress = etDeliveryAddress.getText().toString().trim();
                tvPhone.setText(phone);
                tvEmail.setText(email);
                tvFullName.setText(recipientName);
                txtAdress.setText(deliveryAddress);
                Log.d("Email", email);
                // Kiểm tra dữ liệu
                if (recipientName.isEmpty() || email.isEmpty() || phone.isEmpty() || deliveryAddress.isEmpty()) {
                    Toast.makeText(PayMentActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PayMentActivity.this, "Thông tin giao hàng đã được lưu!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}

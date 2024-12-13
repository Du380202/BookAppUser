package com.example.appbansach.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.Adapter.ChiTietDonHangAdapter;
import com.example.appbansach.R;
import com.example.appbansach.api.OrderApi;
import com.example.appbansach.model.ApiResponse;
import com.example.appbansach.model.OrderDetail;
import com.example.appbansach.model.ResponseHistoryOrderModel;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.DialogCallBack;
import com.example.appbansach.utils.DialogUtils;
import com.example.appbansach.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietDonHangActivity extends AppCompatActivity {
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    ImageView backIcon;
    TextView maDonHang, totalItems, totalCost, tvKH, tvDC, tvTrangThai, tvEmail, tvPhone, tvPayment;
    RecyclerView recyclerViewChiTietDonHang;
    Button btnHuy, btnNhanHang;
    int maDonHangHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        setControl();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        ResponseHistoryOrderModel order = new ResponseHistoryOrderModel();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            order = (ResponseHistoryOrderModel) bundle.getSerializable("Object:", ResponseHistoryOrderModel.class);
            Log.d("Order", order.toString());
        }
        innitData(order);
        setEvent(order.getOrderId());
    }

    private void setEvent(Integer orderId) {
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Bạn chắc chắn muốn hủy đơn hàng này?";
                DialogUtils.showWarningDialog(ChiTietDonHangActivity.this, message, new DialogCallBack() {
                    @Override
                    public void onConfirm() {
                        cancelOrder(orderId);
                    }
                });
            }
        });

        btnNhanHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Bạn chắc chắn đã nhận đơn hàng này?";
                DialogUtils.showWarningDialog(ChiTietDonHangActivity.this, message, new DialogCallBack() {
                    @Override
                    public void onConfirm() {
                        successOrder(orderId);
                    }
                });
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void cancelOrder(Integer orderId) {
        RetrofitClient retrofitClient = new RetrofitClient();
        OrderApi orderApi = retrofitClient.getRetrofit().create(OrderApi.class);
        orderApi.cancelOrder(orderId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    DialogUtils.showSuccessDialog(ChiTietDonHangActivity.this, apiResponse.getMessage(), new DialogCallBack() {
                        @Override
                        public void onConfirm() {
                            tvTrangThai.setText("Đã hủy");
                        }
                    });
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ApiResponse errorResponse = gson.fromJson(response.errorBody().string(), ApiResponse.class);
                            DialogUtils.showErrorDialog(ChiTietDonHangActivity.this, errorResponse.getMessage());
                        } else {
                            Log.e("Error", "HTTP Status: " + response.code() + ", Message: Không có nội dung lỗi");
                        }
                    } catch (IOException e) {
                        Log.e("Error", e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("ErrorCancelOrder", t.getMessage());
            }
        });
    }

    private void successOrder(int orderId) {
        RetrofitClient retrofitClient = new RetrofitClient();
        OrderApi orderApi = retrofitClient.getRetrofit().create(OrderApi.class);
        orderApi.successOrder(orderId, 2).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    DialogUtils.showSuccessDialog(ChiTietDonHangActivity.this, apiResponse.getMessage(), new DialogCallBack() {
                        @Override
                        public void onConfirm() {
                            tvTrangThai.setText("Hoàn thành");
                        }
                    });
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Gson gson = new Gson();
                            ApiResponse errorResponse = gson.fromJson(response.errorBody().string(), ApiResponse.class);
                            DialogUtils.showErrorDialog(ChiTietDonHangActivity.this, errorResponse.getMessage());
                        } else {
                            Log.e("Error", "HTTP Status: " + response.code() + ", Message: Không có nội dung lỗi");
                        }
                    } catch (IOException e) {
                        Log.e("Error", e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void innitData(ResponseHistoryOrderModel order) {
        maDonHang.setText(String.format("Mã đơn hàng: %d", order.getOrderId()));
        int totalQuantity = 0;
        DecimalFormat dft = new DecimalFormat("###,###,###");
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            totalQuantity += orderDetail.getQuantity();
        }
        totalItems.setText(String.format("%d sản phẩm", totalQuantity));
        totalCost.setText(String.format("Thành tiền: đ%s", dft.format(order.getTotalPrice())));
        tvKH.setText(String.format(order.getFullName()));
        tvDC.setText(String.format(order.getAddress()));
        tvEmail.setText(String.format(order.getEmail()));
        tvPhone.setText(order.getPhoneNumber());
        tvTrangThai.setText(order.getStatus() == 0 ? "Chờ xác nhận" : order.getStatus() == 1 ? "Đang giao" : order.getStatus() == 2 ? "Đã giao" : "Đã hủy");
        if (order.getToken() != null) {
            tvPayment.setText("Đã thanh toán");
        }
        else {
            tvPayment.setText("Chưa thanh toán");
        }
        // Hiển thị danh sách chi tiết đơn hàng
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );

        ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(this, order.getOrderDetails());
        recyclerViewChiTietDonHang.setLayoutManager(layoutManager);
        recyclerViewChiTietDonHang.setAdapter(chiTietDonHangAdapter);

        if (order.getStatus() == 3 || order.getStatus() == 2) {
            btnHuy.setEnabled(false);
            btnNhanHang.setEnabled(false);
            btnNhanHang.setAlpha(0.5f);
            btnHuy.setAlpha(0.5f);
        }
    }


    private void setControl() {
        maDonHang = findViewById(R.id.maDonHang);
        totalItems = findViewById(R.id.totalItems);
        totalCost = findViewById(R.id.totalCost);
        tvKH = findViewById(R.id.tvFullName);
        tvDC = findViewById(R.id.tvDeliveryAddress);
        tvTrangThai = findViewById(R.id.tvTrangThai);
        recyclerViewChiTietDonHang = findViewById(R.id.recyclerViewChiTietDonHang);
        btnNhanHang = findViewById(R.id.btnNhanHang);
        btnHuy = findViewById(R.id.btnHuy);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvPayment = findViewById(R.id.tvPayment);
        backIcon = findViewById(R.id.backIcon);
    }
}
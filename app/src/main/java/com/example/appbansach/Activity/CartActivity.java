package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

//import com.example.appbansach.Adapter.GioHangAdapter;
import com.example.appbansach.Adapter.GioHangAdapter;
import com.example.appbansach.R;
import com.example.appbansach.api.CartApi;
import com.example.appbansach.model.Cart;
import com.example.appbansach.retrofit.ApiBanSach;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;
import com.example.appbansach.model.EventBus.TinhTongEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    TextView soLuongSp, tvDiscount, tvTotalPrice, tvSubtotal;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView backIcon;
    Button btnMuahang;
    GioHangAdapter gioHangAdapter;
    List<Cart> cartList = new ArrayList<>();
    BigDecimal totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Utils.mangGioHang.clear();
        setControl();
        getData();
        tinhTongTien();
        initControl();
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private void tinhTongTien() {
        totalPrice = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for(Cart cart : Utils.mangGioHang){
            if(cart.getBook().getDiscountedPrice().compareTo(BigDecimal.valueOf(0)) == 1) {
                totalDiscount = totalDiscount.add((cart.getBook().getPrice().subtract(cart.getBook().getDiscountedPrice())).multiply(BigDecimal.valueOf(cart.getQuantity())));
                totalPrice = totalPrice.add(cart.getBook().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));

            } else {
                totalPrice = totalPrice.add(cart.getBook().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
            }
//            Log.d("TinhTongTien", "Total Price Item: " + cart.getQuantity());
//            Log.d("TinhTongGia", "Tong Gia Item"+ cart.getPrice());
        }
        Log.d("TinhTongTien", "Total Price: " + totalPrice + " " + totalDiscount);
        updateTotalPriceUI(totalDiscount);
    }

    private void initControl() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
            gioHangAdapter = new GioHangAdapter(getApplicationContext(),Utils.mangGioHang);
            recyclerView.setAdapter(gioHangAdapter);


        btnMuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.mangGioHang.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), PayMentActivity.class);
                    intent.putExtra("totalPrice",totalPrice.toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CartActivity.this, "Giỏ hàng rỗng", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void setControl() {
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        soLuongSp = findViewById(R.id.textView14);
        btnMuahang = findViewById(R.id.btnOrder);
        recyclerView = findViewById(R.id.cartView);
        tvDiscount = findViewById(R.id.tvDiscount);
        backIcon = findViewById(R.id.backIcon);
        tvSubtotal = findViewById(R.id.tvSubtotal);

    }


    private void getData() {
        RetrofitClient retrofitClient = new RetrofitClient();
        CartApi cartApi = retrofitClient.getRetrofit().create(CartApi.class);
        cartApi.getCart(Utils.user_current.getUserId()).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    cartList = response.body();
                    Utils.mangGioHang.clear();
                    Utils.mangGioHang.addAll(cartList);
                    gioHangAdapter.notifyDataSetChanged();
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                    tinhTongTien();
                    Log.d("CartActivity", "Cart data loaded successfully");
                    for (Cart cart : cartList) {
                        Log.d("CartItem2", cart.toString());
                    }
                } else {
                    Log.e("CartActivity", "Failed to load cart data");
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("CartActivity", "Error: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if(event != null){
            Log.d("EventBus", "TinhTongEvent received");
            tinhTongTien();
        }
    }
    private void updateTotalPriceUI(BigDecimal totalDiscount) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String formattedPrice = decimalFormat.format(totalPrice);
        tvSubtotal.setText(String.valueOf("đ" + formattedPrice));
        tvDiscount.setText(String.valueOf("-đ" + decimalFormat.format(totalDiscount)));
        totalPrice = totalPrice.subtract(totalDiscount);
        tvTotalPrice.setText(String.valueOf("đ" + decimalFormat.format(totalPrice)));
    }
}
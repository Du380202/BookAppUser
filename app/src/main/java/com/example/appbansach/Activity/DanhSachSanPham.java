package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.appbansach.Adapter.DanhSachSanPhamAdapter;
import com.example.appbansach.R;
import com.example.appbansach.api.BookApi;
import com.example.appbansach.api.CartApi;
import com.example.appbansach.model.Book;
import com.example.appbansach.model.Cart;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachSanPham extends AppCompatActivity {
    private DanhSachSanPhamAdapter danhSachSanPhamAdapter;
    private RecyclerView recyclerViewDssp;
    private List<Book> bookList;
    private ImageView imageView, backIcon;
    FrameLayout frameLayout;
    NotificationBadge badge;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_san_pham);
        setControl();
        loadCartFromDatabase();
        Intent intent = getIntent();
        int receivedNumber = intent.getIntExtra("categoryId", 0);
        if (receivedNumber == 0) {
            setEvent();
        }
        else {
            getBookByCategory(receivedNumber);
        }
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

    }

    private void getBookByCategory(Integer categoryId) {
        RetrofitClient retrofitClient = new RetrofitClient();
        BookApi bookApi = retrofitClient.getRetrofit().create(BookApi.class);
        bookApi.getAllBookByCategory(categoryId).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
//                bookList.clear();
                bookList = response.body();
                danhSachSanPhamAdapter = new DanhSachSanPhamAdapter(bookList,getApplicationContext());
                recyclerViewDssp.setAdapter(danhSachSanPhamAdapter);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DanhSachSanPham.this, SearchActivity.class));
            }
        });
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DanhSachSanPham.this, CartActivity.class));
            }
        });
    }

    private void loadCartFromDatabase() {
        RetrofitClient retrofitClient = new RetrofitClient();
        CartApi cartApi = retrofitClient.getRetrofit().create(CartApi.class);
        cartApi.getCart(Utils.user_current.getUserId()).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    Utils.mangGioHang = response.body();
                    updateBadge();
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                // Handle failure to load cart
            }
        });
    }

    private void updateBadge() {
        int totalItem = 0;
        for (Cart cart : Utils.mangGioHang) {
            totalItem += cart.getQuantity();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void setControl() {
        recyclerViewDssp = findViewById(R.id.recycleviewDanhSachSp);
        imageView = findViewById(R.id.imgSearch);
        badge = findViewById(R.id.menu_giohang);
        backIcon = findViewById(R.id.backIcon);
        frameLayout = findViewById(R.id.framegiohang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(giohang);
            }
        });
        if(Utils.mangGioHang != null){
            badge.setText(String.valueOf(Utils.mangGioHang.size()));
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewDssp.setLayoutManager(layoutManager);
        recyclerViewDssp.setHasFixedSize(true);
    }

    private void setEvent() {
        RetrofitClient retrofitClient = new RetrofitClient();
        BookApi bookApi = retrofitClient.getRetrofit().create(BookApi.class);

        bookApi.getAllBook().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                bookList = response.body();
                danhSachSanPhamAdapter = new DanhSachSanPhamAdapter(bookList,getApplicationContext());
                recyclerViewDssp.setAdapter(danhSachSanPhamAdapter);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }

        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DanhSachSanPham.this, SearchActivity.class));
            }
        });
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DanhSachSanPham.this, CartActivity.class));
            }
        });

    }


}
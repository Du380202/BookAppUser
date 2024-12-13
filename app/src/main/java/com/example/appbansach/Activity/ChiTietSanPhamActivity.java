package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appbansach.Adapter.ReviewAdapter;
import com.example.appbansach.Adapter.TabAdapter;
import com.example.appbansach.R;
import com.example.appbansach.api.CartApi;
import com.example.appbansach.api.RateApi;
import com.example.appbansach.model.Book;
import com.example.appbansach.model.Cart;
import com.example.appbansach.model.Rate;
import com.example.appbansach.model.User;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.nex3z.notificationbadge.NotificationBadge;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private ReviewAdapter reviewAdapter;
    TextView tensp, giasp, mota, textViewNoReviews;
    Button btnthem;
    ImageView imgHinhAnh, backIcon;
    Toolbar toolbar;
    Book book;
    RatingBar rating_bar;
    int bookId;
    RecyclerView recycleViewDanhGia;
    NotificationBadge badge;
    List<Rate> rates = new ArrayList<>();
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        setControl();
        loadCartFromDatabase();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang(book);
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }


        private void themGioHang(Book book) {
            boolean productExistsInCart = false;
            for (Cart cartItem : Utils.mangGioHang) {
                if (cartItem.getBook().getBookId() == book.getBookId()) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    productExistsInCart = true;
                    break;
                }
            }

            if (!productExistsInCart) {
                Cart newCartItem = new Cart();
                newCartItem.setBook(book);
                newCartItem.setUserid(Utils.user_current.getUserId());
                newCartItem.setQuantity(1);
                newCartItem.setPrice(new BigDecimal(String.valueOf(book.getPrice())));

                Utils.mangGioHang.add(newCartItem);

            }
            Log.d("API_INPUT", "mangGioHang: " + Utils.mangGioHang.size());
            Log.d("API_INPUT", "Book ID: " + bookId);
            Log.d("API_INPUT", "User ID: " + Utils.user_current.getUserId());

            RetrofitClient retrofitClient = new RetrofitClient();
            CartApi cartApi = retrofitClient.getRetrofit().create(CartApi.class);

            cartApi.addCart(book.getBookId(), Utils.user_current.getUserId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        updateBadge();
                        Toast.makeText(getApplicationContext(), "Thêm giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        // Nếu thất bại
                        Toast.makeText(getApplicationContext(), "Thêm giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }




    private void loadCartFromDatabase() {
        // userId biến tĩnh
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
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            book = (Book) bundle.getSerializable("Object:", Book.class);
            assert book != null;
            tensp.setText(book.getBookName() != null ? book.getBookName() : "Tên sách không có");
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            giasp.setText(decimalFormat.format(book.getPrice()));
            rating_bar.setRating(book.getRating());
            Glide.with(this).load(book.getImage() != null ? book.getImage() : R.drawable.detective).into(imgHinhAnh);
//            getRateByBook(book.getBookId());
            TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(),  FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, book.getBookId());
            viewPager.setAdapter(tabAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }

    }


    private void setControl() {
        tensp = findViewById(R.id.chi_tiet_sp_ten);
        giasp = findViewById(R.id.productPrice);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        imgHinhAnh = findViewById(R.id.imgChitiet);
        toolbar = findViewById(R.id.toolbarChitietSp);
        backIcon = findViewById(R.id.backIcon);
        badge = findViewById(R.id.menu_giohang);
        rating_bar = findViewById(R.id.ratingBarStart);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        FrameLayout frameLayout = findViewById(R.id.framegiohang);
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
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadCartFromDatabase();
    }
}

package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbansach.Adapter.DanhSachSanPhamAdapter;
import com.example.appbansach.R;
import com.example.appbansach.api.BookApi;
import com.example.appbansach.model.Book;
import com.example.appbansach.retrofit.ApiBanSach;
import com.example.appbansach.retrofit.RetrofitClient;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {


    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edtSearch;
    DanhSachSanPhamAdapter danhSachSanPhamAdapter;
    List<Book> bookList ;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setControl();
        loadCartFromDatabase();

    }

    private void loadCartFromDatabase() {

    }

    private void setControl() {
        bookList = new ArrayList<>();
        edtSearch = findViewById(R.id.edtSearch);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menuSoLuong);
        recyclerView = findViewById(R.id.recyclerViewDSSanPhamTimKiem);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 cột
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 0) {
                    danhSachSanPhamAdapter = new DanhSachSanPhamAdapter(bookList,getApplicationContext());
                    recyclerView.setAdapter(danhSachSanPhamAdapter);
                } else {
                    getDataSearch(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    private void getDataSearch(String string) {
        RetrofitClient retrofitClient = new RetrofitClient();
        BookApi bookApi = retrofitClient.getRetrofit().create(BookApi.class);
        String keyword = string.trim();
        Log.d("SearchKeyword", "Searching for: " + keyword);  // Log the search keyword

        bookApi.searchBook(keyword).enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", "Response: " + response.body().toString());
                    bookList = response.body();
                    danhSachSanPhamAdapter = new DanhSachSanPhamAdapter(bookList,getApplicationContext());
                    recyclerView.setAdapter(danhSachSanPhamAdapter);
                } else {
                    Log.e("API Response", "No data or error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                // Log any errors that occur during the API call
                Log.e("API Error", "Error: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

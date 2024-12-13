package com.example.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.appbansach.R;
import com.example.appbansach.api.RateApi;
import com.example.appbansach.model.Book;
import com.example.appbansach.model.Rate;
import com.example.appbansach.model.RateDto;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.DialogCallBack;
import com.example.appbansach.utils.DialogUtils;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhGiaActivity extends AppCompatActivity {
    TextView btnGuiDanhGia;
    EditText edtNhanXet;
    RatingBar ratingBar;
    float ratingValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);
        setControl();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Book book = (Book) bundle.getSerializable("Object:", Book.class);
            Log.d("Book", book.toString());
            btnGuiDanhGia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postData(book.getBookId());
                }
            });
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) { 
                    ratingValue=  rating;
                }
            }
        });



    }

    private void postData(Integer bookId) {
        RateDto rateDto = new RateDto();
        rateDto.setComment(edtNhanXet.getText().toString());
        rateDto.setStart(ratingValue);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            rateDto.setDate(String.valueOf(currentDate));
        }

        rateDto.setBookId(bookId);
        rateDto.setUserId(3);
        RetrofitClient retrofitClient = new RetrofitClient();
        RateApi rateApi = retrofitClient.getRetrofit().create(RateApi.class);
        rateApi.createRate(rateDto).enqueue(new Callback<Rate>() {
            @Override
            public void onResponse(Call<Rate> call, Response<Rate> response) {
                Rate rate = response.body();
                DialogUtils.showSuccessDialog(DanhGiaActivity.this, "Da gui danh gia", new DialogCallBack() {
                    @Override
                    public void onConfirm() {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
                Log.d("Success", "Danh gia thanh cong");
            }

            @Override
            public void onFailure(Call<Rate> call, Throwable t) {
                Log.e("Logg", t.getMessage());
            }
        });
    }

    private void setControl() {
        btnGuiDanhGia = findViewById(R.id.btnGuiDanhGia);
        edtNhanXet = findViewById(R.id.edtNhanXet);
        ratingBar = findViewById(R.id.ratingBar);
    }
}
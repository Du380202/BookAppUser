package com.example.appbansach.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appbansach.Activity.ChiTietSanPhamActivity;
import com.example.appbansach.Adapter.ReviewAdapter;
import com.example.appbansach.R;
import com.example.appbansach.api.RateApi;
import com.example.appbansach.model.Rate;
import com.example.appbansach.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsFragment extends Fragment {
    private RecyclerView recycleViewDanhGia;
    private ReviewAdapter reviewAdapter;
    private List<Rate> rates = new ArrayList<>();
    private Integer bookId;
    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getInt("bookId");
        }
    }

    public static ReviewsFragment newInstance(Integer bookId) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putInt("bookId", bookId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        recycleViewDanhGia = view.findViewById(R.id.reviewView);
        recycleViewDanhGia.setLayoutManager(new LinearLayoutManager(getContext()));

        if (bookId != null) {
            getRateByBook(bookId);
        } else {
            Log.e("ReviewsFragment", "Book ID is null");
        }

        return view;
    }

    private void getRateByBook(Integer bookId) {
        RetrofitClient retrofitClient = new RetrofitClient();
        RateApi rateApi = retrofitClient.getRetrofit().create(RateApi.class);
        rateApi.getRateByBookId(bookId).enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                rates = response.body();
                reviewAdapter = new ReviewAdapter(getContext(), rates);
                recycleViewDanhGia.setAdapter(reviewAdapter);
                Log.d("RateSize", rates.size()+ "");
            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {

            }
        });
    }
}
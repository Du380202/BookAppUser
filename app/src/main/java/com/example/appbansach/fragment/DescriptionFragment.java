package com.example.appbansach.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appbansach.R;
import com.example.appbansach.api.BookApi;
import com.example.appbansach.model.Book;
import com.example.appbansach.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionFragment extends Fragment {
    private Integer bookId;
    Book book = new Book();
    TextView tvMoTa;
    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance(Integer bookId) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putInt("bookId", bookId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getInt("bookId");
        }

    }

    private void getBook(Integer bookId) {
        RetrofitClient retrofitClient = new RetrofitClient();
        BookApi bookApi = retrofitClient.getRetrofit().create(BookApi.class);
        bookApi.getBook(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                book = response.body();
                tvMoTa.setText(book.getDescription());
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        tvMoTa = view.findViewById(R.id.tvMota);

        if (bookId != null) {
            getBook(bookId);
            Log.d("Booojsndfsjd", book.toString());

        } else {
            Log.e("descriptionFragment", "Book ID is null");
        }

        return view;
    }
}
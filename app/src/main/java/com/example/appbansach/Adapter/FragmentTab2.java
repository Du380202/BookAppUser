package com.example.appbansach.Adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbansach.R;
import com.example.appbansach.api.OrderApi;
import com.example.appbansach.model.ResponseHistoryOrderModel;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTab2  extends Fragment {
    private RecyclerView recyclerViewDonHang;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        recyclerViewDonHang = view.findViewById(R.id.recyclerViewDonHang);
        recyclerViewDonHang.setLayoutManager(new LinearLayoutManager(getContext()));
//        apiBanGiay = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanGiay.class);
        getOrders();
        return view;
    }

    private void getOrders() {
        RetrofitClient retrofitClient = new RetrofitClient();
        OrderApi orderApi = retrofitClient.getRetrofit().create(OrderApi.class);
        Log.d("RequestHistory", "Request Data: " + Utils.user_current.getUserId());
        orderApi.getHistoryOrders(Utils.user_current.getUserId()).enqueue(new Callback<List<ResponseHistoryOrderModel>>() {
            @Override
            public void onResponse(Call<List<ResponseHistoryOrderModel>> call, Response<List<ResponseHistoryOrderModel>> response) {
                if(response.isSuccessful()){
                    List<ResponseHistoryOrderModel> responseHistoryOrderModelList = new ArrayList<>();
                    List<ResponseHistoryOrderModel> donhangChoXacNhan = new ArrayList<>();
                    responseHistoryOrderModelList = response.body();
                    for (int i = 0; i < responseHistoryOrderModelList.size() ;i++){
                        if(responseHistoryOrderModelList.get(i).getStatus() == 0){
                            donhangChoXacNhan.add(responseHistoryOrderModelList.get(i));
                        }
                    }
                    DonHangAdapter donHangAdapter = new DonHangAdapter(getContext(),donhangChoXacNhan);
                    recyclerViewDonHang.setAdapter(donHangAdapter);
                }else{
                    Toast.makeText(getContext(),"Lấy đơn hàng thất bại",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResponseHistoryOrderModel>> call, Throwable t) {
                Log.e("ErrorTab1", t.getMessage());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getOrders();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}

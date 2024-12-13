package com.example.appbansach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbansach.Activity.ChiTietDonHangActivity;
import com.example.appbansach.R;
import com.example.appbansach.model.Order;
import com.example.appbansach.model.OrderDetail;
import com.example.appbansach.model.ResponseHistoryOrderModel;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<ResponseHistoryOrderModel> responseHistoryOrderModelList;

    public DonHangAdapter(Context context, List<ResponseHistoryOrderModel> responseHistoryOrderModelList) {
        this.context = context;
        this.responseHistoryOrderModelList = responseHistoryOrderModelList;
    }
    @NonNull
    @Override
    public DonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangAdapter.MyViewHolder holder, int position) {
        ResponseHistoryOrderModel donHang = responseHistoryOrderModelList.get(position);
        DecimalFormat dft = new DecimalFormat("###,###,###");
        Log.d("OrderId", donHang.getOrderId()+"");
        // Hiển thị thông tin đơn hàng
        holder.txtdonhang.setText(String.format("Đơn hàng: %d", donHang.getOrderId()));
        int totalQuantity = 0;
        for (OrderDetail orderDetail : donHang.getOrderDetails())   //  21/11/2024 chua lây tổng số lượng sản phẩm trong đơn hàng
        {
            totalQuantity += orderDetail.getQuantity();
        }
        holder.txtTotalItems.setText(String.format("%d sản phẩm", totalQuantity)); // chưa hiển thị tổng số sản pẩm
        holder.txtTotalcost.setText(String.format("Thành tiền: đ%s", dft.format(donHang.getTotalPrice())));
        if(donHang.getStatus() == 0){
            holder.txtStatus.setText("Chờ xác nhận");
        }else if(donHang.getStatus() == 1){
            holder.txtStatus.setText("Đang giao");
        }else if(donHang.getStatus() == 2){
            holder.txtStatus.setText("Đã giao");
        }else if(donHang.getStatus() == 3){
            holder.txtStatus.setText("Đã hủy");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerViewchitiet.getContext(),
                LinearLayoutManager.VERTICAL, false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getOrderDetails().size());

        // Adapter cho danh sách chi tiết đơn hàng
        ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(context, donHang.getOrderDetails());
        holder.recyclerViewchitiet.setLayoutManager(layoutManager);
        holder.recyclerViewchitiet.setAdapter(chiTietDonHangAdapter);
        holder.recyclerViewchitiet.setRecycledViewPool(viewPool);

        holder.itemView.setOnClickListener(v -> {
//            intent.putExtra("orderId", donHang.getOrderId());
//            intent.putExtra("totalPrice", donHang.getTotalPrice().toString());
//            intent.putExtra("status", donHang.getStatus());
//            intent.putExtra("orderDetails", new Gson().toJson(donHang.getOrderDetails())); // Chuyển danh sách sang JSON string
//            intent.putExtra("fullName", donHang.getFullName());
//            intent.putExtra("address", donHang.getAddress());
            Intent intent = new Intent(context, ChiTietDonHangActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Object:" , donHang);
            intent.putExtras(bundle);
//            ((OrderActivity) context).startActivityForResult(detail, HttpCodeUtils.REQUEST_SUCCESS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return responseHistoryOrderModelList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtdonhang, txtStatus, txtTotalItems, txtTotalcost;
        RecyclerView recyclerViewchitiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            txtStatus = itemView.findViewById(R.id.trangThai);
            txtTotalItems = itemView.findViewById(R.id.totalItems);
            txtTotalcost = itemView.findViewById(R.id.totalCost);
            recyclerViewchitiet = itemView.findViewById(R.id.recyclerview_chitiet);
        }
    }
}

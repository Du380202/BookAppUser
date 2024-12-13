package com.example.appbansach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbansach.Activity.ChiTietSanPhamActivity;
import com.example.appbansach.Activity.DanhGiaActivity;
import com.example.appbansach.R;
import com.example.appbansach.model.Book;
import com.example.appbansach.model.OrderDetail;

import java.text.DecimalFormat;
import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.MyViewHolder> {

    public ChiTietDonHangAdapter(Context context, List<OrderDetail> orderDetailList) {
        this.context = context;
        this.orderDetailList = orderDetailList;
    }

    Context context;
    List<OrderDetail> orderDetailList;

    @NonNull
    @Override
    public ChiTietDonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chi_tiet_don_hang,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietDonHangAdapter.MyViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);
        DecimalFormat dft = new DecimalFormat("###,###,###");
        holder.txtten.setText(orderDetail.getBook().getBookName());
        holder.txtsoluong.setText(String.format("x%d", orderDetail.getQuantity()));
        String formattedPrice = dft.format(orderDetail.getBook().getPrice());
        holder.txtgia.setText(formattedPrice);

        Glide.with(context).load(orderDetail.getBook().getImage()).into(holder.imagechitiet);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(context, DanhGiaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Object:", orderDetail.getBook());
                detail.putExtras(bundle);
                context.startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
      return  orderDetailList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imagechitiet;
        TextView txtten, txtsoluong,txtgia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagechitiet = itemView.findViewById(R.id.item_imgchitiet);
            txtten = itemView.findViewById(R.id.item_tenspchitiet);
            txtsoluong = itemView.findViewById(R.id.item_soluongchitiet);
            txtgia = itemView.findViewById(R.id.item_giachitiet);
        }
    }
}

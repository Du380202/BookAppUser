package com.example.appbansach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbansach.Activity.ChiTietSanPhamActivity;
import com.example.appbansach.Interface.ItemClickListener;
import com.example.appbansach.R;
import com.example.appbansach.model.Book;

import java.text.DecimalFormat;
import java.util.List;

public class DanhSachSanPhamAdapter extends RecyclerView.Adapter<DanhSachSanPhamAdapter.ViewHolder> {

    private List<Book> bookList;
    private Context context;
    public DanhSachSanPhamAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public DanhSachSanPhamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danh_sach_san_pham, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachSanPhamAdapter.ViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tendssp.setText(book.getBookName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.giadssp.setText(decimalFormat.format(book.getPrice()));
        holder.ratingBarStart.setRating(book.getRating());
        holder.txtStart.setText(String.valueOf(book.getRating()));
        Glide.with(context).load(book.getImage()).into(holder.imgdssp);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {
                    onclickDetail(book);
                }
            }
        });
    }

    private void onclickDetail(Book book) {
        Intent detail = new Intent(context, ChiTietSanPhamActivity.class);
        detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Object:", book);
        detail.putExtras(bundle);
        context.startActivity(detail);
    }



    @Override
    public int getItemCount() {
//       return bookList.size();
        return (bookList != null) ? bookList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView tendssp, giadssp, txtStart;
        ImageView imgdssp;
        RatingBar ratingBarStart;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStart = itemView.findViewById(R.id.txtStart);
            imgdssp = itemView.findViewById(R.id.imgBook);
            ratingBarStart = itemView.findViewById(R.id.ratingBarStart);
            tendssp = itemView.findViewById(R.id.titleBook);
            giadssp = itemView.findViewById(R.id.txtPrice);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
}

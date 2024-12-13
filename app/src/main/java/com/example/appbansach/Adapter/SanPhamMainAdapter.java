package com.example.appbansach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class SanPhamMainAdapter extends RecyclerView.Adapter<SanPhamMainAdapter.Viewholder> {

    private List<Book> bookList;
    private Context context;
    public SanPhamMainAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pop_list,parent,false);
        context = parent.getContext();
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamMainAdapter.Viewholder holder, int position) {
        Book book = bookList.get(position);
        holder.titleBook.setText(getLimitedWords(book.getBookName(), 4));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPrice.setText(String.valueOf("đ" + decimalFormat.format(book.getPrice())));
        if(book.getDiscountedPrice() != null) {
            if (book.getDiscountedPrice().compareTo(BigDecimal.valueOf(0)) == 1) {
                holder.txtDiscount.setVisibility(View.VISIBLE);
                holder.txtPrice.setTextColor(Color.GRAY);
                holder.txtPrice.setTextSize(12);
                holder.txtDiscount.setText(String.valueOf("đ" + decimalFormat.format(book.getDiscountedPrice())));
            }
            else {
                holder.txtDiscount.setVisibility(View.GONE);
            }
        }

        holder.ratingBar.setRating(book.getRating());
        holder.txtRating.setText(String.valueOf(book.getRating()));
        Glide.with(context).load(bookList.get(position).getImage()).into(holder.imgBook);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    onclickDetail(book);
                }
            }
        });
    }
    private String getLimitedWords(String text, int wordLimit) {
        String[] words = text.split("\\s+");
        if (words.length > wordLimit) {
            return TextUtils.join(" ", Arrays.copyOfRange(words, 0, wordLimit)) + "...";
        }
        return text;
    }

    private void onclickDetail(Book book) {
        Intent detail = new Intent(context, ChiTietSanPhamActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Object:", book);
        detail.putExtras(bundle);
        context.startActivity(detail);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class Viewholder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleBook,txtPrice, txtRating, txtDiscount;
        RatingBar ratingBar;
        ImageView imgBook;
        private ItemClickListener itemClickListener;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            txtRating = itemView.findViewById(R.id.txtStart);
            imgBook = itemView.findViewById(R.id.imgBook);
            ratingBar = itemView.findViewById(R.id.ratingBarStart);
            titleBook = itemView.findViewById(R.id.titleBook);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDiscount = itemView.findViewById(R.id.txtDiscount);
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

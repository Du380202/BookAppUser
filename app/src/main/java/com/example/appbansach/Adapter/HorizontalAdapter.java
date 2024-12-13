package com.example.appbansach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbansach.Activity.DanhSachSanPham;
import com.example.appbansach.R;
import com.example.appbansach.model.Category;

import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Context context;
    private List<Category> itemList;

    // Constructor nhận dữ liệu
    public HorizontalAdapter(Context context, List<Category> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy item hiện tại
        Category currentItem = itemList.get(position);
        holder.textView.setText(currentItem.getCategoryName());
        Glide.with(holder.itemView.getContext())
                .load(currentItem.getCategoryImg())
                .override(24, 28) // Đặt chiều rộng và chiều cao cố định
                .centerCrop()
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail(currentItem.getCategoryId());
            }
        });

    }

    private void detail(Integer categoryId) {
        Intent intent = new Intent(context, DanhSachSanPham.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("categoryId", categoryId); // "key_number" là khóa, có thể thay đổi tùy ý
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView5);
            textView = itemView.findViewById(R.id.textView7);
        }
    }
}

package com.example.appbansach.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbansach.R;
import com.example.appbansach.model.Rate;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Rate> reviewList;

    public ReviewAdapter(Context context, List<Rate> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_danh_gia, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Rate review = reviewList.get(position);
        holder.username.setText(review.getUser().getFullName());
        holder.ratingBar.setRating(review.getStart());
        holder.ratingDate.setText(review.getDate());
        holder.tvComment.setText(review.getComment());
//        Glide.with(holder.itemView.getContext())
//                .load(review.getUser().getAvatar())
//                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView username, ratingDate, tvComment;
        RatingBar ratingBar;
        ImageView profileImage;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            ratingDate = itemView.findViewById(R.id.ratingDate);
            tvComment = itemView.findViewById(R.id.tvComment);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}

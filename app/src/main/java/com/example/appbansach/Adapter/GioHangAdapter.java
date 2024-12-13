package com.example.appbansach.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbansach.Interface.IImageClickListenner;
import com.example.appbansach.R;
import com.example.appbansach.api.BookApi;
import com.example.appbansach.api.CartApi;
import com.example.appbansach.model.ApiResponse;
import com.example.appbansach.model.Book;
import com.example.appbansach.model.Cart;
import com.example.appbansach.model.EventBus.TinhTongEvent;
import com.example.appbansach.retrofit.RetrofitClient;
import com.example.appbansach.utils.DialogUtils;
import com.example.appbansach.utils.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.greenrobot.eventbus.EventBus;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHodler> {

    Context context;
    List<Cart> cartList;

    public GioHangAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_cart,parent,false);
        return new ViewHodler(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        RetrofitClient retrofitClient = new RetrofitClient();
        CartApi cartApi = retrofitClient.getRetrofit().create(CartApi.class);
        Cart cart = cartList.get(position);

        holder.ten_sp_cart.setText(cart.getBook().getBookName());
        holder.sl_sp_cart.setText(String.valueOf(cart.getQuantity()));
        Glide.with(context).load(cart.getBook().getImage()).into(holder.img_cart);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_cart_giasp1.setText(decimalFormat.format(cart.getBook().getPrice()));

        BigDecimal quantity = BigDecimal.valueOf(cart.getQuantity());
        BigDecimal price;
        if (cart.getBook().getDiscountedPrice().compareTo(BigDecimal.valueOf(0)) == 1) {
            price = cart.getBook().getDiscountedPrice();
            BigDecimal total = price.multiply(quantity);
            holder.item_cart_giasp2.setText(decimalFormat.format(total));
        } else {
            price = cart.getBook().getPrice();
            BigDecimal total = price.multiply(quantity);
            holder.item_cart_giasp2.setText(decimalFormat.format(total));
        }


        holder.setListenner(new IImageClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1) {
                    if (cart.getQuantity() > 1) {
                        cart.setQuantity(cart.getQuantity() - 1);
                        cartApi.removeCart(cartList.get(position).getCartId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Cập nhật giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                    holder.sl_sp_cart.setText(String.valueOf(cart.getQuantity()));
                                    BigDecimal newTotal = price.multiply(BigDecimal.valueOf(cart.getQuantity()));
                                    holder.item_cart_giasp2.setText(decimalFormat.format(newTotal));
                                    EventBus.getDefault().postSticky(new TinhTongEvent());
                                    Log.d("EventBus", "TinhTongEvent posted");
                                } else {
                                    Toast.makeText(context, "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Remove product from cart
                        cartApi.removeCart(cartList.get(position).getCartId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Sản phẩm đã được xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                                    if (position < cartList.size()) {
                                        cartList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, cartList.size());
                                    }
                                    EventBus.getDefault().postSticky(new TinhTongEvent());
                                    Log.d("EventBus", "TinhTongEvent posted");
                                } else {
                                    Toast.makeText(context, "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else if (giatri == 2) {
                    cart.setQuantity(cart.getQuantity() + 1);
                    cartApi.addCart(cartList.get(position).getBook().getBookId(), Utils.user_current.getUserId()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(context, "Cập nhật giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                holder.sl_sp_cart.setText(String.valueOf(cart.getQuantity()));
                                BigDecimal newTotal = price.multiply(BigDecimal.valueOf(cart.getQuantity()));
                                holder.item_cart_giasp2.setText(decimalFormat.format(newTotal));
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            } else {
                                try {
                                    if (response.errorBody() != null) {
                                        Gson gson = new Gson();
                                        ApiResponse errorResponse = gson.fromJson(response.errorBody().string(), ApiResponse.class);
                                        Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        cart.setQuantity(cart.getQuantity() - 1);
//                                        Log.e("Error", "HTTP Status: " +  + ", Message: Không có nội dung lỗi");
                                    } else {
                                        Log.e("Error", "HTTP Status: " + response.code() + ", Message: Không có nội dung lỗi");
                                    }
                                } catch (IOException e) {
                                    Log.e("Error", e.getMessage(), e);
                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView item_cart_tru, item_cart_cong,ten_sp_cart, sl_sp_cart,item_cart_giasp1, item_cart_giasp2;
        ImageView img_cart;

        IImageClickListenner listenner;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            item_cart_tru = itemView.findViewById(R.id.item_tru);
            item_cart_cong = itemView.findViewById(R.id.item_cong);
            ten_sp_cart = itemView.findViewById(R.id.tvBookTitle);
            item_cart_giasp1 = itemView.findViewById(R.id.tvPrice);
            item_cart_giasp2 = itemView.findViewById(R.id.tvTotalPrice);
            sl_sp_cart = itemView.findViewById(R.id.tvQuantity);
            img_cart = itemView.findViewById(R.id.bookImg);

            // event click
            item_cart_tru.setOnClickListener(this);
            item_cart_cong.setOnClickListener(this);
        }

        public void setListenner(IImageClickListenner listenner) {
            this.listenner = listenner;
        }

        @Override
        public void onClick(View v) {

            if(v == item_cart_tru){
                // 1 tru
                listenner.onImageClick(v,getAdapterPosition(),1);

            }else if(v == item_cart_cong){
                // 2 cong
                listenner.onImageClick(v,getAdapterPosition(),2);
            }
        }
    }
}

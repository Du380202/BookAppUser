package com.example.appbansach.retrofit;

import com.example.appbansach.model.Book;
import com.example.appbansach.model.DanhSachSanPhamMoiModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiBanSach {

    // danh sách sản phẩm main
    @GET("book")
    Observable<DanhSachSanPhamMoiModel> getSanPhamMoiChinh();
}

package com.example.appbansach.model;

import java.io.Serializable;
import java.util.List;

public class SanPham implements Serializable {
    private String tenSanPham;
    private String moTa;
    private int soLuong;
    private String hinhAnh;
    private String giaSanPham;

    public SanPham(String tenSanPham, String moTa, int soLuong, String hinhAnh, String giaSanPham) {
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.soLuong = soLuong;
        this.hinhAnh = hinhAnh;
        this.giaSanPham = giaSanPham;
    }

    public String getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(String giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }



    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }




}

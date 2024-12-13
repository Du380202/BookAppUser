package com.example.appbansach.model;

public class DanhGia {

    private int maDanhGia;
    private int soSao;
    private String nhanXet;
    private int maDonHang;
    private String ngayDanhGia;
//    private String email;
    private String username;
    private String dungVoiMoTa;
    private String chatLuongSanPham;

    public String getDungVoiMoTa() {
        return dungVoiMoTa;
    }

    public void setDungVoiMoTa(String dungVoiMoTa) {
        this.dungVoiMoTa = dungVoiMoTa;
    }

    public String getChatLuongSanPham() {
        return chatLuongSanPham;
    }

    public void setChatLuongSanPham(String chatLuongSanPham) {
        this.chatLuongSanPham = chatLuongSanPham;
    }

    public int getMaDanhGia() {
        return maDanhGia;
    }

    public void setMaDanhGia(int maDanhGia) {
        this.maDanhGia = maDanhGia;
    }

    public int getSoSao() {
        return soSao;
    }

    public void setSoSao(int soSao) {
        this.soSao = soSao;
    }

    public String getNhanXet() {
        return nhanXet;
    }

    public void setNhanXet(String nhanXet) {
        this.nhanXet = nhanXet;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getNgayDanhGia() {
        return ngayDanhGia;
    }

    public void setNgayDanhGia(String ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "DanhGia{" +
                "maDanhGia=" + maDanhGia +
                ", soSao=" + soSao +
                ", dungVoiMoTa='" + dungVoiMoTa + '\'' +
                ", chatLuongSanPham='" + chatLuongSanPham + '\'' +
                ", nhanXet='" + nhanXet + '\'' +
                ", maDonHang=" + maDonHang +
                ", ngayDanhGia='" + ngayDanhGia + '\'' +
                '}';
    }
}

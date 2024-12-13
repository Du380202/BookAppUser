package com.example.appbansach.utils;

import com.example.appbansach.model.Cart;
import com.example.appbansach.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {

//    public static  final String BASE_URL= "http://10.252.8.168:8080/api/";

    // Gio hang su dung bien toan cuc luu
    public  static List<Cart> mangGioHang = new ArrayList<>();
    public  static List<Cart> mangMuaHang = new ArrayList<>();

    // Base image url
    public static final String BASE_IMAGE_URL = "images/";
    public static User user_current = new User(); // bien cuc bo dung o dang ky, dnag nhap
}

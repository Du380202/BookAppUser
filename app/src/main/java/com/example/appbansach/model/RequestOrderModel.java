package com.example.appbansach.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RequestOrderModel {
    private String orderDate;
    private String email;
    private String address;
    private String fullName;
    private Integer status;
    private Integer userId;
    private String phoneNumber;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    @Override
    public String toString() {
        return "RequestOrderModel{" +
                "orderDate=" + orderDate +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", fullName='" + fullName + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                '}';
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

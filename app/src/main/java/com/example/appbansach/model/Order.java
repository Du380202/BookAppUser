package com.example.appbansach.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Order {
    private Integer orderId;
    private String orderDate;
    private BigDecimal totalPrice;
    private String email;
    private String address;
    private String fullName;
    private Integer status;
    private String token;
    private List<OrderDetail> orderDetails;
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }



    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate='" + orderDate + '\'' +
                ", totalPrice=" + totalPrice +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", fullName='" + fullName + '\'' +
                ", status=" + status +
                ", token='" + token + '\'' +
                ", orderDetails=" + orderDetails +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}

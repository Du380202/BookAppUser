package com.example.appbansach.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderDetail implements Serializable {
    private Integer id;
    private Integer quantity;
    private BigDecimal totalPrice;
//    private List<Book> bookList;
    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }




}

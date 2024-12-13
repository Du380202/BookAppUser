package com.example.appbansach.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Book implements Serializable {
    private int bookId;
    private String bookName;
    private BigDecimal price;
    private String image;
    private String description;
    private int quantity;
    private int pubblicationYear;
    private int status;
    private  float rating;
    private String daySaleDate;
    private BigDecimal discountedPrice ;

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getDaySaleDate() {
        return daySaleDate;
    }

    public void setDaySaleDate(String daySaleDate) {
        this.daySaleDate = daySaleDate;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPubblicationYear() {
        return pubblicationYear;
    }

    public void setPubblicationYear(int pubblicationYear) {
        this.pubblicationYear = pubblicationYear;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" +bookName + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", description=" + description +
                ", quantity='" + quantity + '\'' +
                ", pubblicationYear='" + pubblicationYear + '\'' +
                ", status=" + status+ '\'' +
                ", rating=" + rating+
                '}';
    }

}

package model;

import java.time.LocalDate;

public class Order {
    private Long id;
    private Long userId;
    private String bookTitle;
    private String bookAuthor;
    private Integer quantity;
    private Integer totalPrice;
    private LocalDate timestamp;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public String toString(){
        return "Order id: " + id + " Book Title: " + bookTitle + " Book Author: " + bookAuthor + " Quantity: " + quantity + " Total Price: " + totalPrice;
    }
}

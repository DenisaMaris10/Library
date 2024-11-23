package model.builder;

import model.Order;

import java.time.LocalDate;

public class OrderBuilder {

    private Order order;

    public OrderBuilder(){
        order = new Order();
    }

    public OrderBuilder setId(Long id){
        order.setId(id);
        return this;
    }

    public OrderBuilder setUserId(Long userId){
        order.setUserId(userId);
        return this;
    }

    public OrderBuilder setBookAuthor(String bookAuthor){
        order.setBookAuthor(bookAuthor);
        return this;
    }
    public OrderBuilder setBookTitle(String bookTitle){
        order.setBookTitle(bookTitle);
        return this;
    }

    public OrderBuilder setQuantity(Integer quantity){
        order.setQuantity(quantity);
        return this;
    }

    public OrderBuilder setTotalPrice(Integer totalPrice){
        order.setTotalPrice(totalPrice);
        return this;
    }

    public OrderBuilder setTimestamp(LocalDate timestamp){
        order.setTimestamp(timestamp);
        return this;
    }

    public Order build(){
        return order;
    }

}

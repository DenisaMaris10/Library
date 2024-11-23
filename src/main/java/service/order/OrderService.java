package service.order;


import model.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order findById(Long id);  //aici nu mai trebuie sa fie cu Optional?
    List<Order> findByUserId(Long id);
    boolean save(Order book);
}

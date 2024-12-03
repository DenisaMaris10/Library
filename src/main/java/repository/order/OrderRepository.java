package repository.order;

import model.Book;
import model.Order;
import model.UserReport;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findAll();
    Optional<Order> findById(Long id);
    List<Order> findByUserId(Long id);
    boolean save(Order order);

}

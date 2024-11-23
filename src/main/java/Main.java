import database.DatabaseConnectionFactory;
import model.Order;
import model.builder.OrderBuilder;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import service.order.OrderService;
import service.order.OrderServiceImpl;

import javax.crypto.spec.PSource;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args){
        Order order = new OrderBuilder().setId(10L)
                .setUserId(1L)
                .setBookAuthor("Liviu Rebreanu")
                .setBookTitle("Ion")
                .setQuantity(3)
                .setTotalPrice(100)
                .setTimestamp(LocalDate.now())
                .build();

        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        OrderRepository orderRepository = new OrderRepositoryMySQL(connection);
        OrderService orderService = new OrderServiceImpl(orderRepository);

        orderService.save(order);

        List<Order> orders = orderService.findAll();
        System.out.println(orders);

        System.out.println(order);
    }
}

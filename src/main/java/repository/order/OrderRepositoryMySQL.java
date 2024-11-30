package repository.order;

import model.Book;
import model.Order;
import model.UserReport;
import model.builder.OrderBuilder;
import model.builder.UserReportBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static database.Constants.Tables.ORDER;
import static database.Constants.Tables.USER;

public class OrderRepositoryMySQL implements OrderRepository{
    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM `" + ORDER +"`;";
        List<Order> orders = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                orders.add(getOrderFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return orders;
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException{
        return new OrderBuilder().setId(resultSet.getLong("id"))
                .setBookAuthor(resultSet.getString("book_author"))
                .setBookTitle(resultSet.getString("book_title"))
                .setQuantity(resultSet.getInt("quantity"))
                .setTotalPrice(resultSet.getInt("total_price"))
                .setTimestamp(new java.sql.Date(resultSet.getDate("timestamp").getTime()).toLocalDate())
                .build();
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql  = "SELECT * FROM `" + ORDER +"` WHERE id=" + id;

        Optional<Order> order = Optional.empty();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                order = Optional.of(getOrderFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public List<Order> findByUserId(Long id) {
        String sql = "SELECT * FROM `" + ORDER + "` WHERE user_id=" + id;
        List<Order> orders = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                orders.add(getOrderFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public boolean save(Order order) {
        String newSql = "INSERT INTO `" + ORDER +"` VALUES(null, ?, ?, ?, ?, ?, ?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setLong(1, order.getUserId());
            preparedStatement.setString(2, order.getBookAuthor());
            preparedStatement.setString(3, order.getBookTitle());
            preparedStatement.setInt(4, order.getQuantity());
            preparedStatement.setInt(5, order.getTotalPrice());
            preparedStatement.setDate(6, java.sql.Date.valueOf(order.getTimestamp()));

            int rowsInserted = preparedStatement.executeUpdate();

            return (rowsInserted != 1) ? false : true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}

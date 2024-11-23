package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository{
    private final Connection connection;
    public BookRepositoryMySQL(Connection connection)
    {
        this.connection = connection;
    }    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";
        List<Book> books = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql  = "SELECT * FROM book WHERE id=" + id;

        Optional<Book> book = Optional.empty();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                book = Optional.of(getBookFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public boolean save(Book book) {

        String newSql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, ?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setInt(3, book.getPrice());
            preparedStatement.setInt(4, book.getStock());
            preparedStatement.setDate(5, java.sql.Date.valueOf(book.getPublishedDate()));

            int rowsInserted = preparedStatement.executeUpdate();

            return (rowsInserted != 1) ? false : true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Book book) {
        String newSql = "DELETE FROM book WHERE author=\'" + book.getAuthor() +"\' AND title=\'" + book.getTitle()+"\';";


        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(newSql);

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(Book book) {
        String sql  = "SELECT * FROM book WHERE title=\'" + book.getTitle() + "\' and author=\'" + book.getAuthor() + "\';";

        Optional<Book> b = Optional.empty();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                b = Optional.of(getBookFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public boolean update(Book book) {
        String sql = "UPDATE book SET stock = " + book.getStock() +  " WHERE id = + " + book.getId() + ";";
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder().setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPrice(resultSet.getInt("price"))
                .setStock(resultSet.getInt("stock"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .build();
    }
}

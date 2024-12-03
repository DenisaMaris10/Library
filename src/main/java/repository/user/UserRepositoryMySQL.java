package repository.user;
import model.Book;
import model.Role;
import model.User;
import model.builder.BookBuilder;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static database.Constants.Tables.BOOK;
import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM `" + USER + "`";
        List<User> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                users.add(getUserFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    // SQL Injection Attacks should not work after fixing functions
    // Be careful that the last character in sql injection payload is an empty space
    // alexandru.ghiurutan95@gmail.com' and 1=1; --
    // ' or username LIKE '%admin%'; --

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            PreparedStatement findStatement = connection.prepareStatement("Select * from `" + USER + "` where `username`= ? and `password`= ?");
            findStatement.setString(1, username);
            findStatement.setString(2, password);
            findStatement.executeQuery();

            ResultSet userResultSet = findStatement.getResultSet();
            if(userResultSet.next()){
                User user = new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                findByUsernameAndPasswordNotification.setResult(user);
            }else{
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database");
        }

        return findByUsernameAndPasswordNotification;
    }

    //pentru a introduce un utilizator nou in baza de date
    @Override
    public Notification<Boolean> save(User user) {
        Notification<Boolean> result = new Notification<>();
        try {

            Notification<User> alreadyExists = findByUsernameAndPassword(user.getUsername(), user.getPassword());
            if  (alreadyExists.hasErrors()) {
                PreparedStatement insertUserStatement = connection
                        .prepareStatement("INSERT INTO `" + USER + "` values (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS); //Statement.RETURN_GENERATED_KEYS ne va returna id-ul pe care l-a atribuit user-ului
                insertUserStatement.setString(1, user.getUsername());
                insertUserStatement.setString(2, user.getPassword());
                insertUserStatement.setString(3, user.getFirstRole().getRole());
                insertUserStatement.executeUpdate();

                ResultSet rs = insertUserStatement.getGeneratedKeys();
                rs.next();
                long userId = rs.getLong(1);
                user.setId(userId);

                rightsRolesRepository.addRolesToUser(user, user.getRoles());

                result.setResult(Boolean.TRUE);
            }
            else {
                result.setResult(Boolean.FALSE);
                result.addError("Already exists a user with this username.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.setResult(Boolean.FALSE);
        }

        return result;

    }

    @Override
    public boolean delete(User user) {
        String sql = "DELETE FROM `" + USER + "` WHERE username=\'" + user.getUsername()+"\';";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
//            Statement statement = connection.createStatement();
            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`= ?";
            PreparedStatement existsStatement = connection.prepareStatement(fetchUserSql);
            existsStatement.setString(1, email);
            ResultSet userResultSet = existsStatement.executeQuery();
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        try {
            return new UserBuilder().setId(resultSet.getLong("id"))
                    .setUsername(resultSet.getString("username"))
                    .setPassword(resultSet.getString("password"))
                    .setRoles(new ArrayList<>())
                    .setFirstRole(new Role(resultSet.getString("role")))
                    .build();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            int a = 0;
        }
        return null;
    }

}
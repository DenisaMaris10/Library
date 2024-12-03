package repository.report;

import model.UserReport;
import model.builder.UserReportBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ORDER;
import static database.Constants.Tables.USER;

public class ReportGenerationRepositoryMySQL implements ReportGenerationRepository{
    private final Connection connection;

    public ReportGenerationRepositoryMySQL(Connection connection){
        this.connection = connection;
    }
    @Override
    public List<UserReport> findAllOrderedByUserIdInAMonth(String month) {
        String sql = "select " + USER + ".id, " + USER + ".username, `" + ORDER + "`.total_price, `" + ORDER + "`.timestamp\n" +
                "from `" + ORDER + "` inner join " + USER + " on `" + ORDER + "`.user_id = " + USER + ".id\n" +
                "where lower(" + USER + ".role) = 'employee' and monthname(`" + ORDER + "`.timestamp) = '" + month + "'\n" +
                "order by " + USER + ".id ASC;";

        List<UserReport> userReports = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                userReports.add(getUserReportsFromResultSetTimestamp(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userReports;
    }

    @Override
    public List<UserReport> findAllOrdersWithSum(String month) {
        String sql = "select " + USER + ".id, " + USER + ".username, sum(`" + ORDER + "`.total_price) as \"Incasari\"\n" +
                "from `" + ORDER + "` inner join " + USER  + " on `" + ORDER + "`.user_id = " + USER + ".id \n" +
                "where lower(" + USER + ".role) = 'employee'\n" +
                "group by " + USER + ".id  \n" +
                "order by sum(`" + ORDER + "`.total_price) DESC";

        List<UserReport> userReports = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                userReports.add(getUserReportsFromResultSet(resultSet));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userReports;
    }

    private UserReport getUserReportsFromResultSet(ResultSet resultSet) throws SQLException{
        return new UserReportBuilder().setUserId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setTotalPrice(resultSet.getInt("Incasari"))
                .build();
    }



    private UserReport getUserReportsFromResultSetTimestamp(ResultSet resultSet) throws SQLException{
        return new UserReportBuilder().setUserId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setTotalPrice(resultSet.getInt("total_price"))
                .setTimestamp(new java.sql.Date(resultSet.getDate("timestamp").getTime()).toLocalDate())
                .build();
    }
}

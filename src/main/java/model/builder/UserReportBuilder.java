package model.builder;

import model.User;
import model.UserReport;

import java.time.LocalDate;

public class UserReportBuilder {

    private UserReport userReport;

    public UserReportBuilder(){
        userReport = new UserReport();
    }

    public UserReportBuilder setUserId(Long id){
        userReport.setUserId(id);
        return this;
    }

    public UserReportBuilder setUsername(String username){
        userReport.setUsername(username);
        return this;
    }

    public UserReportBuilder setTotalPrice(Integer totalPrice){
        userReport.setTotalPrice(totalPrice);
        return this;
    }

    public UserReportBuilder setTimestamp(LocalDate timestamp){
        userReport.setTimestamp(timestamp);
        return this;
    }

    public UserReport build(){
        return userReport;
    }
}

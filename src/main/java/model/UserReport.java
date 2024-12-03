package model;

import java.time.LocalDate;

public class UserReport {
    private Long userId;
    private String username;
    private Integer totalPrice;
    private LocalDate timestamp;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }
}

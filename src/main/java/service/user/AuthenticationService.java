package service.user;

import model.Role;
import model.User;
import model.validator.Notification;

public interface AuthenticationService {
    Notification<Boolean> genericRegister(String username, String password, Role role);
    Notification<Boolean> customerRegister(String username, String password);

    Notification<User> login(String username, String password);

    boolean logout(User user);
}

package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import launcher.EmployeeComponentFactory;
import javafx.scene.Scene;
import launcher.AdminComponentFactory;
import launcher.EmployeeComponentFactory;
import launcher.LoginComponentFactory;
import model.Role;
import model.User;
//import model.validator.Notification;
import model.validator.Notification;
import model.validator.UserValidator;
import service.user.AuthenticationService;
import view.BookView;
import view.LoginView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

public class LoginController {

    // in controller nu trebuie sa avem nimic legat de repository
    private final LoginView loginView;
    private final AuthenticationService authenticationService;


    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);
            if (loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("LogIn Successfull!");
                User user = loginNotification.getResult();
                if (user.getFirstRole().getRole().equals(ADMINISTRATOR)){
                    AdminComponentFactory instance = AdminComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage(), loginNotification.getResult().getId());
                }
                else if (user.getFirstRole().getRole().equals(EMPLOYEE)){
                    EmployeeComponentFactory instance = EmployeeComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage(), loginNotification.getResult().getId()); //trimitem catre controller id-ul userului; avem nevoie de el pentru functionalitatea de sale (userId din order)
                }
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.customerRegister(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}
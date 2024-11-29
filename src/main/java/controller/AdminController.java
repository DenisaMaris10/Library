package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import mapper.BookMapper;
import mapper.UserMapper;
import model.Role;
import model.validator.Notification;
import repository.security.RightsRolesRepository;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import service.user.UserService;
import view.AdminView;
import view.model.BookDTO;
import view.model.UserDTO;
import view.model.builder.BookDTOBuilder;
import view.model.builder.UserDTOBuilder;

import javax.swing.event.MenuListener;

import static database.Constants.Roles.*;

public class AdminController {
    private final AdminView adminView;
    private final UserService userService;
    private final RightsRolesRepository rightsRolesRepository;  /** aici trebuie sa modific sa fac un service cred**/
    private final AuthenticationService authenticationService;
    private Long userId;
    public AdminController(AdminView adminView, UserService userService, RightsRolesRepository rightsRolesRepository, AuthenticationService authenticationService, Long userId){
        this.adminView = adminView;
        this.userService = userService;
        this.rightsRolesRepository = rightsRolesRepository;
        this.authenticationService = authenticationService;
        this.userId = userId;

        this.adminView.addAddButtonListener(new AddButtonListener());
        this.adminView.addDeleteButtonListener(new DeleteButtonListener());
        this.adminView.addReportButtonListener(new ReportButtonListener());
        this.adminView.addRolesComboBoxListener(new RolesComboBoxListener());
    }

    private class AddButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();
            Role role = adminView.getRole();

            if(username.isEmpty() || password.isEmpty()){
                adminView.addDisplayAlertMessage("Save Error", "Problem at Username and Password", "Can not have any empty field");
            }
            else{
                if (role == null) {
                    adminView.setRole(rightsRolesRepository.findRoleByTitle("ADMINISTRATOR"));
                    role = adminView.getRole();
                }
                UserDTO userDTO = new UserDTOBuilder().setUsername(username).setRole(role).build();
                Notification<Boolean> registerNotification = authenticationService.genericRegister(username, password, role);
                if (registerNotification.hasErrors()) {
                    adminView.setActionTargetText(registerNotification.getFormattedErrors());
                } else {
                    adminView.addDisplayAlertMessage("Save Successful", "User added", "User was successfully added");
                    adminView.addUserObservableList(userDTO);
                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            UserDTO userDTO = (UserDTO) adminView.getUserTableView().getSelectionModel().getSelectedItem();
            if(userDTO != null){
                boolean deletionSuccessful = userService.delete((UserMapper.convertUserDTOToUser(userDTO)));
                if(deletionSuccessful){
                    adminView.addDisplayAlertMessage("Delete Successful", "User deleted", "User was successfully deleted");
                    adminView.removeBookFromObservableList(userDTO);
                }
                else {
                    adminView.addDisplayAlertMessage("Delete Error", "Problem at deleting user", "There was a problem with the delete. Please try again");
                }
            }
            else {
                adminView.addDisplayAlertMessage("Delete Error", "Problem at deleting user", "You must select a user before pressing the delete button");
            }
        }
    }

    private class ReportButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {

        }
    }

    private class RolesComboBoxListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            String selectedRole = adminView.getSelectedRole();
            Role role = rightsRolesRepository.findRoleByTitle(selectedRole);
            adminView.setRole(role);
        }
    }
}

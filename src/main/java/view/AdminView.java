package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Role;
import view.model.UserDTO;

import java.util.List;

public class AdminView {
    private TableView userTableView;
    private final ObservableList<UserDTO> usersObservableList;
    private TextField usernameTextField;
    private TextField passwordTextField; // sau PasswordField
    private Label usernameLabel;
    private Label passwordLabel;
    private Text actionTarget;
    private Button addButton;
    private Button deleteButton;
    private Button reportButton;
    private Button logOutButton;
    ComboBox<String> rolesComboBox;
    private Stage primaryStage;
    private Role role;

    public AdminView(Stage primaryStage, List<UserDTO> users){
        primaryStage.setTitle("Employee");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 800, 800);
        primaryStage.setScene(scene);

        usersObservableList = FXCollections.observableArrayList(users);
        initTableView(gridPane);
        initAddUserOptions(gridPane);
        initReportAndLogout(gridPane);
        initRoleMenu(gridPane);
        initActionTarget(gridPane);
        this.primaryStage = primaryStage;
        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane){
        userTableView = new TableView<UserDTO>();
        userTableView.setPlaceholder(new Label("No users to be displayed"));
        TableColumn<UserDTO, Long> idColumn = new TableColumn<>("UserId");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserDTO, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        userTableView.getColumns().addAll(idColumn, usernameColumn, roleColumn);
        userTableView.setItems(usersObservableList);
        gridPane.add(userTableView, 0, 0, 5, 1);
    }

    private void initAddUserOptions(GridPane gridPane){
        usernameLabel = new Label("Username:");
        gridPane.add(usernameLabel, 1, 1);
        usernameTextField = new TextField();
        usernameTextField.setPromptText("username");
        gridPane.add(usernameTextField, 2, 1);

        passwordLabel = new Label("Password:");
        gridPane.add(passwordLabel, 3, 1);
        passwordTextField = new TextField();
        passwordTextField.setPromptText("password");
        gridPane.add(passwordTextField, 4, 1);

        addButton = new Button("Add User");
        gridPane.add(addButton, 5, 1);

        deleteButton = new Button("Delete User");
        gridPane.add(deleteButton, 6, 1);
    }

    private void initReportAndLogout(GridPane gridPane){
        reportButton = new Button("PDF Report");
        gridPane.add(reportButton, 7, 1);

        logOutButton = new Button("LogOut");
        gridPane.add(logOutButton, 4, 2);
    }

    private void initRoleMenu(GridPane gridPane){
        String[] roles = {"ADMINISTRATOR", "EMPLOYEE", "CUSTOMER"};
        rolesComboBox = new ComboBox<String>(FXCollections.observableArrayList(roles));
        rolesComboBox.getSelectionModel().selectFirst();
        gridPane.add(rolesComboBox, 7, 0);
    }

    private void initActionTarget(GridPane gridPane){
        actionTarget = new Text(); //aici se vor scrie mesajele (de ex: daca parola e prea scurta, sau user invalid)
        actionTarget.setFill(Color.FIREBRICK);
        gridPane.add(actionTarget, 1, 6);
    }

    public void addAddButtonListener(EventHandler<ActionEvent> addButtonActionListener){
        addButton.setOnAction(addButtonActionListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonActionListener){
        deleteButton.setOnAction(deleteButtonActionListener);
    }

    public void addReportButtonListener(EventHandler<ActionEvent> reportButtonActionListener){
        reportButton.setOnAction(reportButtonActionListener);
    }

    public void addRolesComboBoxListener(EventHandler<ActionEvent> rolesComboBoxActionListener){
        rolesComboBox.setOnAction(rolesComboBoxActionListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public void addUserObservableList(UserDTO user){
        usersObservableList.add(user);
    }

    public void removeBookFromObservableList(UserDTO user){
        usersObservableList.remove(user);
    }

    public void setActionTargetText(String text){ this.actionTarget.setText(text);}

    public String getUsername(){
        return usernameTextField.getText();
    }

    public String getPassword(){
        return passwordTextField.getText();
    }

    public Role getRole(){
        return role;
    }

    public TableView getUserTableView() {
        return userTableView;
    }

    public String getSelectedRole(){
        return rolesComboBox.getValue();
    }

    public Stage getStage(){
        return primaryStage;
    }

    public void setRole(Role role){
        this.role = role;
    }

}

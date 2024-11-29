package view.model;

import javafx.beans.property.*;

public class UserDTO {

    private LongProperty id;
    public void setId(Long id){
        idProperty().set(id);
    }

    public Long getId(){
        return idProperty().get();
    }
    public LongProperty idProperty(){
        if(id == null){
            id = new SimpleLongProperty(this, "id");
        }
        return id;
    }

    private StringProperty username;
    public void setUsername(String username){
        usernameProperty().set(username);
    }

    public String getUsername(){
        return usernameProperty().get();
    }

    public StringProperty usernameProperty(){
        if(username == null){
            username = new SimpleStringProperty(this, "username");
        }
        return username;
    }

    private StringProperty password;
    public void setPassword(String password){
        passwordProperty().set(password);
    }

    public String getPassword(){
        return passwordProperty().get();
    }

    public StringProperty passwordProperty(){
        if(username == null){
            username = new SimpleStringProperty(this, "username");
        }
        return username;
    }

    private StringProperty role;
    public void setRole(String role){
        roleProperty().set(role);
    }

    public String getRole(){
        return roleProperty().get();
    }

    public StringProperty roleProperty(){
        if (role == null){
            role = new SimpleStringProperty(this, "role");
        }
        return role;
    }
}

package view.model.builder;

import model.Role;
import view.model.UserDTO;

public class UserDTOBuilder {
    private UserDTO userDTO;

    public UserDTOBuilder(){
        userDTO = new UserDTO();
    }

    public UserDTOBuilder setUsername(String username){
        userDTO.setUsername(username);
        return this;
    }

    public UserDTOBuilder setPassword(String password){
        userDTO.setPassword(password);
        return this;
    }

    public UserDTOBuilder setRole(Role role){
        userDTO.setRole(role.getRole());
        return this;
    }

    public UserDTO build(){
        return userDTO;
    }
}

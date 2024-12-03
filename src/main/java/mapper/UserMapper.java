package mapper;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static database.Constants.Roles.CUSTOMER;

public class UserMapper {
    public static UserDTO convertUsertoUserDTO(User user){
        return new UserDTOBuilder().setUsername(user.getUsername()).setRole(user.getFirstRole()).build();
    }

    public static User convertUserDTOToUser(UserDTO userDTO){
        return new UserBuilder().setUsername(userDTO.getUsername())
                .setRoles(null).build();
    }

    public static List<UserDTO> convertUserListToUserDTOList(List<User> users){
        return users.parallelStream().map(UserMapper::convertUsertoUserDTO).collect(Collectors.toList());
    }

    public static List<User> convertUserDTOListToUserList(List<UserDTO> users){
        return users.parallelStream().map(UserMapper::convertUserDTOToUser).collect(Collectors.toList());
    }
}

package model;

import java.util.List;

//Clasa asta este un BEAN
public class User {
    private Long id;
    private String username;
    private String password;
    private List<Role> roles;
    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Role getFirstRole(){
        return roles.getFirst(); // folosesc doar primul element, deoarece momentan ne gandim ca un user are un singur rol
    }

    public void setFirstRole(Role role){
        this.roles.addFirst(role);
    }
}

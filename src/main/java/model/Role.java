package model;

import java.util.List;

public class Role {
    private Long id;
    private String role;
    private List<Right> rights;

    public Role(Long id, String role, List<Right> rights){
        this.id = id;
        this.role = role;
        this.rights = rights;
    }

    public Role(String role){
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    public List<Right> getRights() {
        return rights;
    }

}

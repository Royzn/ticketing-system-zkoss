package latihan.dto;

import latihan.entity.RoleEntity;

public class UserListDto {
    private Long id;
    private String name;
    private RoleEntity role;
    private String username;

    public UserListDto(Long id, String name, RoleEntity role, String username) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

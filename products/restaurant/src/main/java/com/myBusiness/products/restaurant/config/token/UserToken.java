package com.myBusiness.products.restaurant.config.token;

import com.myBusiness.products.restaurant.entity.MenuBarEntity;
import com.myBusiness.products.restaurant.entity.RoleEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class UserToken implements Serializable {

    private static final long serialVersionUID = 1085049078946151940L;

    private Long userId;

    private String name;

    private Long role;

    private List<MenuBarEntity> menuBars;

    private boolean admin;

    public UserToken() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    public List<MenuBarEntity> getMenuBars() {
        return menuBars;
    }

    public void setMenuBars(List<MenuBarEntity> menuBars) {
        this.menuBars = menuBars;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}

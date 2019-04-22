package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-09-24
 */
@TableName("menu_bar_for_role")
public class MenuBarForRoleEntity implements Serializable {

    private static final long serialVersionUID = -5911973116386320903L;

    @TableId("role_id")
    private Long roleId;

    @TableField("menu_bar_id")
    private Long menuBarId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuBarId() {
        return menuBarId;
    }

    public void setMenuBarId(Long menuBarId) {
        this.menuBarId = menuBarId;
    }

    @Override
    public String toString() {
        return "MenuBarForRoleEntity{" +
        "roleId=" + roleId +
        ", menuBarId=" + menuBarId +
        "}";
    }
}

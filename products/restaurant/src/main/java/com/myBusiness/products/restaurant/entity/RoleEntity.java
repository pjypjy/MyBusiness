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
 * @since 2018-09-21
 */
@TableName("role")
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = -7797601510314264996L;

    @TableId("role_id")
    private Long roleId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("order")
    private Integer order;

    @TableField("enable")
    private Boolean enable;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
        "roleId=" + roleId +
        ", name=" + name +
        ", description=" + description +
        ", order=" + order +
        ", enable=" + enable +
        "}";
    }
}

package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-09-17
 */
@TableName("admin_user")
public class AdminUserEntity implements Serializable {

    private static final long serialVersionUID = -9130408447621511314L;

    @TableId("user_id")
    private Long userId;

    @TableField("login_id")
    private String loginId;

    @TableField("name")
    private String name;

    @TableField("phone")
    private String phone;

    @TableId("role_id")
    private Long roleId;

    @TableField("local_password")
    private String localPassword;

    @TableField("password_suffix")
    private String passwordSuffix;

    @TableField("birthday")
    private LocalDate birthday;

    @TableField("sex")
    private String sex;

    @TableField("token")
    private String token;

    @TableField("enable")
    private Boolean enable;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getLocalPassword() {
        return localPassword;
    }

    public void setLocalPassword(String localPassword) {
        this.localPassword = localPassword;
    }
    public String getPasswordSuffix() {
        return passwordSuffix;
    }

    public void setPasswordSuffix(String passwordSuffix) {
        this.passwordSuffix = passwordSuffix;
    }
    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "AdminUserEntity{" +
                "userId=" + userId +
                ", loginId='" + loginId + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", roleId=" + roleId +
                ", localPassword='" + localPassword + '\'' +
                ", passwordSuffix='" + passwordSuffix + '\'' +
                ", birthday=" + birthday +
                ", sex='" + sex + '\'' +
                ", token='" + token + '\'' +
                ", enable=" + enable +
                '}';
    }
}

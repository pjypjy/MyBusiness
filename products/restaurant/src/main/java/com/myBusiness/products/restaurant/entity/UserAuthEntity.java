package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("user_auth")
public class UserAuthEntity implements Serializable {

    private static final long serialVersionUID = 2459796941597292744L;

    @TableId("user_id")
    private Long userId;

    @TableField("phone")
    private String phone;

    @TableField("local_password")
    private String localPassword;

    @TableField("password_suffix")
    private String passwordSuffix;

    @TableField("token")
    private String token;

    @TableField("we_chat_open_id")
    private String weChatOpenId;

    @TableField("we_chat_session_key")
    private String weChatSessionKey;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getWeChatOpenId() {
        return weChatOpenId;
    }

    public void setWeChatOpenId(String weChatOpenId) {
        this.weChatOpenId = weChatOpenId;
    }
    public String getWeChatSessionKey() {
        return weChatSessionKey;
    }

    public void setWeChatSessionKey(String weChatSessionKey) {
        this.weChatSessionKey = weChatSessionKey;
    }

    @Override
    public String toString() {
        return "UserAuthEntity{" +
        "userId=" + userId +
        ", phone=" + phone +
        ", localPassword=" + localPassword +
        ", passwordSuffix=" + passwordSuffix +
        ", token=" + token +
        ", weChatOpenId=" + weChatOpenId +
        ", weChatSessionKey=" + weChatSessionKey +
        "}";
    }
}

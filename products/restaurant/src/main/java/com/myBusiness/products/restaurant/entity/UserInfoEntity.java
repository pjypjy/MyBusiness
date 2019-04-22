package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-09-12
 */
@TableName("user_info")
public class UserInfoEntity implements Serializable {

    private static final long serialVersionUID = 1691724170221756457L;

    @TableId
    private Long userId;

    private String name;

    private LocalDate birthday;

    private String sex;

    private String phone;

    private Integer points;

    private Integer allPoints;

    private BigDecimal balance;

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
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
    public Integer getAllPoints() {
        return allPoints;
    }

    public void setAllPoints(Integer allPoints) {
        this.allPoints = allPoints;
    }
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", points=" + points +
                ", allPoints=" + allPoints +
                ", balance=" + balance +
                '}';
    }
}

package com.myBusiness.products.restaurant.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.myBusiness.common.util.CommonUtil;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-09-26
 */
@TableName("coupon")
public class CouponEntity implements Serializable {

    private static final long serialVersionUID = 3473160345646723098L;

    @TableId("coupon_id")
    private Long couponId;

    @TableField("author_user_id")
    private Long authorUserId;

    @TableField("type")
    private String type;

    @TableField("status")
    private String status;

    @TableField("discount")
    private BigDecimal discount;

    @TableField("offset_money")
    private BigDecimal offsetMoney;

    @TableField("require_money")
    private BigDecimal requireMoney;

    @TableField("create_user_id")
    private Long createUserId;

    @TableField("distribute_user_id")
    private Long distributeUserId;

    @TableField("start_date")
    private LocalDate startDate;

    @TableField("expire_date")
    private LocalDate expireDate;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(Long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = CommonUtil.setRoundHalfUpKeep2DecimalPlaces(discount);
    }
    public BigDecimal getOffsetMoney() {
        return offsetMoney;
    }

    public void setOffsetMoney(BigDecimal offsetMoney) {
        this.offsetMoney = CommonUtil.setRoundHalfUpKeep2DecimalPlaces(offsetMoney);
    }
    public BigDecimal getRequireMoney() {
        return requireMoney;
    }

    public void setRequireMoney(BigDecimal requireMoney) {
        this.requireMoney = CommonUtil.setRoundHalfUpKeep2DecimalPlaces(requireMoney);
    }
    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
    public Long getDistributeUserId() {
        return distributeUserId;
    }

    public void setDistributeUserId(Long distributeUserId) {
        this.distributeUserId = distributeUserId;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "CouponEntity{" +
                "couponId=" + couponId +
                ", authorUserId=" + authorUserId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", discount=" + discount +
                ", offsetMoney=" + offsetMoney +
                ", requireMoney=" + requireMoney +
                ", createUserId=" + createUserId +
                ", distributeUserId=" + distributeUserId +
                ", startDate=" + startDate +
                ", expireDate=" + expireDate +
                '}';
    }
}

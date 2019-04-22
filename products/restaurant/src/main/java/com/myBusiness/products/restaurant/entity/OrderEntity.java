package com.myBusiness.products.restaurant.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.myBusiness.products.restaurant.util.CommonUtil;
import com.myBusiness.products.restaurant.vo.OrderGoodsInfoVo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author pjy
 * @since 2018-10-23
 */
@TableName("order")
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = -5469145453551332108L;

    @TableId("order_id")
    private Long orderId;

    @TableField("user_id")
    private Long userId;

    @TableField("coupon_id")
    private String couponId;

    @TableField("status")
    private String status;

    /**
     * 原价
     */
    @TableField("original_money")
    private BigDecimal originalMoney;

    /**
     * 原价->商品打折 实际需支付的价格
     */
    @TableField("need_pay_money")
    private BigDecimal needPayMoney;

    /**
     * 原价->商品打折->优惠券
     * 最终支付的钱
     */
    @TableField("final_pay_money")
    private BigDecimal finalPayMoney;

    @TableField("prepay_money")
    private BigDecimal prepayMoney;

    @TableField("order_item")
    private String orderItem;

    @TableField("order_remark")
    private String orderRemark;

    @TableField("customer_count")
    private Integer customerCount;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("cancel_id")
    private Long cancelId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public BigDecimal getOriginalMoney() {
        return originalMoney;
    }

    public void setOriginalMoney(BigDecimal originalMoney) {
        this.originalMoney = originalMoney;
    }
    public BigDecimal getNeedPayMoney() {
        return needPayMoney;
    }

    public void setNeedPayMoney(BigDecimal needPayMoney) {
        this.needPayMoney = needPayMoney;
    }
    public BigDecimal getPrepayMoney() {
        return prepayMoney;
    }

    public void setPrepayMoney(BigDecimal prepayMoney) {
        this.prepayMoney = prepayMoney;
    }
    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }
    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }
    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public Long getCancelId() {
        return cancelId;
    }

    public void setCancelId(Long cancelId) {
        this.cancelId = cancelId;
    }

    public BigDecimal getFinalPayMoney() {
        return finalPayMoney;
    }

    public void setFinalPayMoney(BigDecimal finalPayMoney) {
        this.finalPayMoney = finalPayMoney;
    }

    public List<OrderGoodsInfoVo> getGoodsInfoFromOrderItem(){
        return CommonUtil.getOrderGoodsInfoVoFromJson(this.orderItem);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", couponId='" + couponId + '\'' +
                ", status='" + status + '\'' +
                ", originalMoney=" + originalMoney +
                ", needPayMoney=" + needPayMoney +
                ", finalPayMoney=" + finalPayMoney +
                ", prepayMoney=" + prepayMoney +
                ", orderItem='" + orderItem + '\'' +
                ", orderRemark='" + orderRemark + '\'' +
                ", customerCount=" + customerCount +
                ", createDate=" + createDate +
                ", cancelId=" + cancelId +
                '}';
    }
}

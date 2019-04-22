package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-10-27
 */
@TableName("order_cancel")
public class OrderCancelEntity implements Serializable {

    private static final long serialVersionUID = -1184785144819102707L;

    @TableId("cancel_id")
    private Long cancelId;

    @TableField("user_id")
    private Long userId;

    @TableField("cancel_date")
    private LocalDateTime cancelDate;

    @TableField("cancel_reason")
    private String cancelReason;

    @TableField("cancel_remark")
    private String cancelRemark;

    public Long getCancelId() {
        return cancelId;
    }

    public void setCancelId(Long cancelId) {
        this.cancelId = cancelId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public LocalDateTime getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDateTime cancelDate) {
        this.cancelDate = cancelDate;
    }
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
    public String getCancelRemark() {
        return cancelRemark;
    }

    public void setCancelRemark(String cancelRemark) {
        this.cancelRemark = cancelRemark;
    }

    @Override
    public String toString() {
        return "OrderCancelEntity{" +
        "cancelId=" + cancelId +
        ", userId=" + userId +
        ", cancelDate=" + cancelDate +
        ", cancelReason=" + cancelReason +
        ", cancelRemark=" + cancelRemark +
        "}";
    }
}

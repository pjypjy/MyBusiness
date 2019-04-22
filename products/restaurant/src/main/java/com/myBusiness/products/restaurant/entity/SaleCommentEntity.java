package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-11-08
 */
@TableName("sale_comment")
public class SaleCommentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("goods_id")
    private Integer goodsId;

    @TableField("order_id")
    private Long orderId;

    @TableField("user_id")
    private Long userId;

    @TableField("comment")
    private String comment;

    @TableField("comment_date")
    private LocalDate commentDate;

    @TableField("review")
    private String review;

    @TableField("review_date")
    private LocalDate reviewDate;

    @TableField("score")
    private Integer score;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
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
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public LocalDate getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(LocalDate commentDate) {
        this.commentDate = commentDate;
    }
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "SaleCommentEntity{" +
        "goodsId=" + goodsId +
        ", orderId=" + orderId +
        ", userId=" + userId +
        ", comment=" + comment +
        ", commentDate=" + commentDate +
        ", review=" + review +
        ", reviewDate=" + reviewDate +
        ", score=" + score +
        "}";
    }
}

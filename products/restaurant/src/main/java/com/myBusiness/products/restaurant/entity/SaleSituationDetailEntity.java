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
 * @since 2018-11-05
 */
@TableName("sale_situation_detail")
public class SaleSituationDetailEntity implements Serializable {

    private static final long serialVersionUID = 827361247555456506L;

    @TableId("goods_id")
    private Integer goodsId;

    @TableField("date")
    private LocalDate date;

    @TableField("amount")
    private Integer amount;

    @TableField("score_5")
    private Integer score5;

    @TableField("score_4")
    private Integer score4;

    @TableField("score_3")
    private Integer score3;

    @TableField("score_2")
    private Integer score2;

    @TableField("score_1")
    private Integer score1;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Integer getScore5() {
        return score5;
    }

    public void setScore5(Integer score5) {
        this.score5 = score5;
    }
    public Integer getScore4() {
        return score4;
    }

    public void setScore4(Integer score4) {
        this.score4 = score4;
    }
    public Integer getScore3() {
        return score3;
    }

    public void setScore3(Integer score3) {
        this.score3 = score3;
    }
    public Integer getScore2() {
        return score2;
    }

    public void setScore2(Integer score2) {
        this.score2 = score2;
    }
    public Integer getScore1() {
        return score1;
    }

    public void setScore1(Integer score1) {
        this.score1 = score1;
    }

    @Override
    public String toString() {
        return "SaleSituationDetailEntity{" +
        "goodsId=" + goodsId +
        ", date=" + date +
        ", amount=" + amount +
        ", score5=" + score5 +
        ", score4=" + score4 +
        ", score3=" + score3 +
        ", score2=" + score2 +
        ", score1=" + score1 +
        "}";
    }
}

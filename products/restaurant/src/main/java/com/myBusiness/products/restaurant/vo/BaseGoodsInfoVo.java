package com.myBusiness.products.restaurant.vo;

import java.io.Serializable;

public class BaseGoodsInfoVo implements Serializable {

    private static final long serialVersionUID = -6048909057152314032L;

    protected int goodsId;

    protected int amount;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BaseGoodsInfo{" +
                "goodsId=" + goodsId +
                ", amount=" + amount +
                '}';
    }
}

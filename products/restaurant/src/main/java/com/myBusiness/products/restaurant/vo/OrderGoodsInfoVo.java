package com.myBusiness.products.restaurant.vo;

import com.myBusiness.common.util.CommonUtil;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderGoodsInfoVo extends BaseGoodsInfoVo implements Serializable {

    private static final long serialVersionUID = -6097047122903655935L;

    private BigDecimal originalPrice;

    private BigDecimal realPrice;

    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = CommonUtil.setRoundHalfUpKeep2DecimalPlaces(realPrice);
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = CommonUtil.setRoundHalfUpKeep2DecimalPlaces(originalPrice);
    }

    @Override
    public String toString() {
        return "OrderGoodsInfo{" +
                "originalPrice=" + originalPrice +
                ", realPrice=" + realPrice +
                ", goodsId=" + goodsId +
                '}';
    }
}

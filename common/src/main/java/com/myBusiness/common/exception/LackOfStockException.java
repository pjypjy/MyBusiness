package com.myBusiness.common.exception;

import java.io.Serializable;

public class LackOfStockException extends Exception implements Serializable {

    private static final long serialVersionUID = 8387460495758066737L;

    private Long goodsId;

    private String goodsName;

    public LackOfStockException(long goodsId,String goodsName) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }
}

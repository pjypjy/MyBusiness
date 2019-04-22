package com.myBusiness.products.restaurant.vo;

import java.io.Serializable;

public class GoodsInfoVo extends BaseGoodsInfoVo implements Serializable {

    private static final long serialVersionUID = -5286064748030037099L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GoodsInfoVo{" +
                "name='" + name + '\'' +
                ", goodsId=" + goodsId +
                ", amount=" + amount +
                '}';
    }
}

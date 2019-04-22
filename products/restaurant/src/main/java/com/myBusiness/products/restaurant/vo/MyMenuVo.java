package com.myBusiness.products.restaurant.vo;

import com.myBusiness.products.restaurant.entity.MyMenuEntity;

import java.io.Serializable;
import java.util.List;

public class MyMenuVo extends MyMenuEntity implements Serializable {

    private static final long serialVersionUID = 7862705014173909668L;

    private List<GoodsInfoVo> goodsInfos;

    public List<GoodsInfoVo> getGoodsInfos() {
        return goodsInfos;
    }

    public void setGoodsInfos(List<GoodsInfoVo> goodsInfos) {
        this.goodsInfos = goodsInfos;
    }
}

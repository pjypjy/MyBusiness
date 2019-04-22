package com.myBusiness.products.restaurant.util;

import com.google.gson.reflect.TypeToken;
import com.myBusiness.products.restaurant.vo.GoodsInfoVo;
import com.myBusiness.products.restaurant.vo.OrderGoodsInfoVo;

import java.math.BigDecimal;
import java.util.List;

public class CommonUtil extends com.myBusiness.common.util.CommonUtil {

    private static BigDecimal bigDecimalOf0 = new BigDecimal(0);

    private static BigDecimal bigDecimalOf10 = new BigDecimal(10);

    public static List<OrderGoodsInfoVo> getOrderGoodsInfoVoFromJson(String json){
        return gson.fromJson(json, new TypeToken<List<OrderGoodsInfoVo>>(){}.getType());
    }

    public static List<GoodsInfoVo> getGoodsInfoVoFromJson(String json){
        return gson.fromJson(json, new TypeToken<List<GoodsInfoVo>>(){}.getType());
    }

    public static BigDecimal getBigDecimalOf_10() {
        return bigDecimalOf10;
    }

    public static BigDecimal getBigDecimalOf_0() {
        return bigDecimalOf0;
    }
}

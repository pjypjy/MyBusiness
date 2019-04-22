package com.myBusiness.products.restaurant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.entity.GoodsEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-09-25
 */
public interface GoodsService extends IService<GoodsEntity> {

    public ResponseResult dealDiscountStatus(Integer goodsId, String type, BigDecimal discount);

    public ResponseResult changeAmount(int goodsId,int changeAmount);

    public ResponseResult dealAmountStatus(int goodsId,String type);

    public ResponseResult dealGoodsStatus(int goodsId, String type);

    public Page<GoodsEntity> pageQuery(int pageNo, int pageSize, Integer categoryId, String name);

    public void testTransaction();
}

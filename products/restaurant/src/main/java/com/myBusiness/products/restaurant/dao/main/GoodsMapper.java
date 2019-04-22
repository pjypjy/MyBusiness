package com.myBusiness.products.restaurant.dao.main;

import com.myBusiness.products.restaurant.entity.GoodsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pjy
 * @since 2018-09-25
 */
public interface GoodsMapper extends BaseMapper<GoodsEntity> {

    public int changeAmount(int goodsId,int changeAmount);

    public int testDySql(int goodsId,int changeAmount);
}

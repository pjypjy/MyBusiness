package com.myBusiness.products.restaurant.dao.main;

import com.myBusiness.products.restaurant.entity.GoodsCategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pjy
 * @since 2018-09-26
 */
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategoryEntity> {

    /**
     *
     * @return @Nullable
     */
    public Boolean exists(int goodsCategoryId);
}

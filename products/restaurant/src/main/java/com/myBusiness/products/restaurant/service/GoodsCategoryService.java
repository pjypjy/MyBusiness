package com.myBusiness.products.restaurant.service;

import com.myBusiness.products.restaurant.entity.GoodsCategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-09-26
 */
public interface GoodsCategoryService extends IService<GoodsCategoryEntity> {


    public Boolean exists(int categoryId);
}

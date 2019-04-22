package com.myBusiness.products.restaurant.service.impl;

import com.myBusiness.products.restaurant.entity.GoodsCategoryEntity;
import com.myBusiness.products.restaurant.dao.main.GoodsCategoryMapper;
import com.myBusiness.products.restaurant.service.GoodsCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-09-26
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategoryEntity> implements GoodsCategoryService {

    @Autowired
    GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public Boolean exists(int categoryId) {
        return goodsCategoryMapper.exists(categoryId);
    }
}

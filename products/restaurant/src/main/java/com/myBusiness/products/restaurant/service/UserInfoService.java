package com.myBusiness.products.restaurant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.products.restaurant.entity.UserInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-09-12
 */
public interface UserInfoService extends IService<UserInfoEntity> {

    public Page<UserInfoEntity> page(int pageNo, int pageSize, String phone);
}

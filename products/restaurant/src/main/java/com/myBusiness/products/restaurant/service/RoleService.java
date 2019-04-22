package com.myBusiness.products.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.products.restaurant.entity.RoleEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-09-21
 */
public interface RoleService extends IService<RoleEntity> {

    public Page<RoleEntity> page(int pageNo, int pageSize, LambdaQueryWrapper<RoleEntity> lambda);

    public int enableOrDisable(long roleId,boolean enable);
}

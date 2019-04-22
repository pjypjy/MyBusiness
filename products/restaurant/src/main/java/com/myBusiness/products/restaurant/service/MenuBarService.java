package com.myBusiness.products.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.products.restaurant.entity.MenuBarEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-09-20
 */
public interface MenuBarService extends IService<MenuBarEntity> {

    public Page<MenuBarEntity> page(int pageNo, int pageSize, LambdaQueryWrapper<MenuBarEntity> lambda);

    public int enableOrDisable(long menuBarId,boolean enable);

    public List<MenuBarEntity> findMenuBarByRoleId(long roleId);
}

package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.products.restaurant.entity.RoleEntity;
import com.myBusiness.products.restaurant.dao.main.RoleMapper;
import com.myBusiness.products.restaurant.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-09-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public Page<RoleEntity> page(int pageNo, int pageSize, LambdaQueryWrapper<RoleEntity> lambda) {
        Page<RoleEntity> page = new Page<>();
        page.setSize(pageNo);
        page.setCurrent(pageSize);
        page.setTotal(roleMapper.selectCount(lambda));
        roleMapper.selectPage(page,lambda);
        return page;
    }

    @Override
    public int enableOrDisable(long roleId, boolean enable) {
        RoleEntity menuBar = new RoleEntity();
        menuBar.setRoleId(roleId);
        menuBar.setEnable(enable);
        return roleMapper.updateById(menuBar);
    }
}

package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.products.restaurant.entity.MenuBarEntity;
import com.myBusiness.products.restaurant.dao.main.MenuBarMapper;
import com.myBusiness.products.restaurant.service.MenuBarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-09-20
 */
@Service
public class MenuBarServiceImpl extends ServiceImpl<MenuBarMapper, MenuBarEntity> implements MenuBarService {

    @Autowired
    MenuBarMapper menuBarMapper;

    @Override
    public Page<MenuBarEntity> page(int pageNo, int pageSize, LambdaQueryWrapper<MenuBarEntity> lambda) {
        Page<MenuBarEntity> page = new Page<>();
        page.setSize(pageNo);
        page.setCurrent(pageSize);
        page.setTotal(menuBarMapper.selectCount(lambda));
        menuBarMapper.selectPage(page,lambda);
        return page;
    }

    @Override
    public int enableOrDisable(long menuBarId, boolean enable) {
        MenuBarEntity menuBar = new MenuBarEntity();
        menuBar.setMenuBarId(menuBarId);
        menuBar.setEnable(enable);
        return menuBarMapper.updateById(menuBar);
    }

    @Override
    public List<MenuBarEntity> findMenuBarByRoleId(long roleId) {
        return menuBarMapper.findMenuBarByRoleId(roleId);
    }
}

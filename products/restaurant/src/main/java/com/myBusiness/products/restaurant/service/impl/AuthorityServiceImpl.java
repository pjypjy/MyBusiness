package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myBusiness.common.exception.RoleNotFoundException;
import com.myBusiness.common.exception.UserNotFoundException;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.dao.main.AdminUserMapper;
import com.myBusiness.products.restaurant.dao.main.MenuBarForRoleMapper;
import com.myBusiness.products.restaurant.dao.main.MenuBarMapper;
import com.myBusiness.products.restaurant.dao.main.RoleMapper;
import com.myBusiness.products.restaurant.entity.AdminUserEntity;
import com.myBusiness.products.restaurant.entity.MenuBarEntity;
import com.myBusiness.products.restaurant.entity.MenuBarForRoleEntity;
import com.myBusiness.products.restaurant.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    AdminUserMapper adminUserMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MenuBarMapper menuBarMapper;

    @Autowired
    MenuBarForRoleMapper menuBarForRoleMapper;

    @Override
    public boolean saveRoleForUser(long roleId, long userId) throws RoleNotFoundException, UserNotFoundException {
        Boolean roleExists = roleMapper.exists(roleId);
        if(!Boolean.TRUE.equals(roleExists)){
            throw new RoleNotFoundException(roleId);
        }
        Boolean userExists = adminUserMapper.exists(userId);
        if(!Boolean.TRUE.equals(userExists)){
            throw new UserNotFoundException(userId);
        }
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setUserId(userId);
        adminUserEntity.setRoleId(roleId);
        return adminUserMapper.updateById(adminUserEntity) > 0;
    }

    @Override
    public ResponseResult saveMenuBarForRole(long menuBarId, long roleId) {
        Boolean roleExists = roleMapper.exists(roleId);
        if(!Boolean.TRUE.equals(roleExists)){
            return ResponseResult.error("该角色不存在");
        }
        MenuBarEntity menuBarEntity = menuBarMapper.selectOne(new QueryWrapper<MenuBarEntity>()
                .lambda()
                .select(MenuBarEntity::getMenuBarId,MenuBarEntity::getName, MenuBarEntity::getParent)
                .eq(MenuBarEntity::getMenuBarId, menuBarId));
        if(menuBarEntity == null){
            return ResponseResult.error("该菜单不存在");
        }
        Long menuBarParentId = menuBarEntity.getParent();
        if(menuBarParentId != null){
            //添加的是二级菜单，需判断 父菜单是否存在，如果不存在则父菜单也一起添加
            Boolean existsParent = menuBarForRoleMapper.exists(roleId, menuBarParentId);
            if(!Boolean.TRUE.equals(existsParent)){
                MenuBarForRoleEntity menuBarForRoleParent = new MenuBarForRoleEntity();
                menuBarForRoleParent.setRoleId(roleId);
                menuBarForRoleParent.setMenuBarId(menuBarParentId);
                menuBarForRoleMapper.insert(menuBarForRoleParent);
            }
        }
        Boolean existsMenuBar = menuBarMapper.exists(menuBarId);
        if(Boolean.TRUE.equals(existsMenuBar)){
            return ResponseResult.error("该菜单已经授权给该角色");
        }
        MenuBarForRoleEntity menuBarForRoleEntity = new MenuBarForRoleEntity();
        menuBarForRoleEntity.setRoleId(roleId);
        menuBarForRoleEntity.setMenuBarId(menuBarEntity.getMenuBarId());
        menuBarForRoleMapper.insert(menuBarForRoleEntity);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult deleteMenuBarForRole(long menuBarId, long roleId) {
        int delete = menuBarForRoleMapper.delete(new QueryWrapper<MenuBarForRoleEntity>()
                .lambda()
                .eq(MenuBarForRoleEntity::getRoleId, roleId)
                .eq(MenuBarForRoleEntity::getMenuBarId, menuBarId));
        return delete > 0 ? ResponseResult.success() : ResponseResult.error("删除失败");
    }
}

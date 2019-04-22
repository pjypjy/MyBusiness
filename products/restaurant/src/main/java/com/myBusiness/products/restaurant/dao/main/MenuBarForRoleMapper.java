package com.myBusiness.products.restaurant.dao.main;

import com.myBusiness.products.restaurant.entity.MenuBarForRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pjy
 * @since 2018-09-24
 */
public interface MenuBarForRoleMapper extends BaseMapper<MenuBarForRoleEntity> {

    /**
     *
     * @return @Nullable
     */
    public Boolean exists(Long roleId, Long menuBarId);
}

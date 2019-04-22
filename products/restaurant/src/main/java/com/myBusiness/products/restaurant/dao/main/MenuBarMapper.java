package com.myBusiness.products.restaurant.dao.main;

import com.myBusiness.products.restaurant.entity.MenuBarEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pjy
 * @since 2018-09-20
 */
public interface MenuBarMapper extends BaseMapper<MenuBarEntity> {

    public List<MenuBarEntity> findMenuBarByRoleId(long roleId);

    /**
     *
     * @param menuBarId
     * @return @Nullable
     */
    public Boolean exists(Long menuBarId);

}

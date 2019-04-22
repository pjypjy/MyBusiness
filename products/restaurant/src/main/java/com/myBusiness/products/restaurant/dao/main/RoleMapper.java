package com.myBusiness.products.restaurant.dao.main;

import com.myBusiness.products.restaurant.entity.RoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pjy
 * @since 2018-09-21
 */
public interface RoleMapper extends BaseMapper<RoleEntity> {

    /**
     *
     * @param roleId
     * @return @Nullable
     */
    public Boolean exists(Long roleId);
}

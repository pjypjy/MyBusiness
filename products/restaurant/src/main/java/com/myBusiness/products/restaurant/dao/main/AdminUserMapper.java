package com.myBusiness.products.restaurant.dao.main;

import com.myBusiness.products.restaurant.entity.AdminUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pjy
 * @since 2018-09-17
 */
public interface AdminUserMapper extends BaseMapper<AdminUserEntity> {

    /**
     *
     * @param userId
     * @return @Nullable
     */
    public Boolean exists(long userId);
}

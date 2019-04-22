package com.myBusiness.products.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.exception.UserNotFoundException;
import com.myBusiness.products.restaurant.entity.AdminUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-09-17
 */
public interface AdminUserService extends IService<AdminUserEntity> {

    public String getToken(long userId) throws UserNotFoundException;

    public int enableOrDisableUser(long userId,boolean enable);

    public AdminUserEntity getById(long userId);

    public Page<AdminUserEntity> page(int pageNo, int pageSize, LambdaQueryWrapper<AdminUserEntity> lambda);

    public AdminUserEntity getDataUnCommit(long userId);

    public AdminUserEntity getDataCommitted();

    public AdminUserEntity getDataRepeatable();

    public AdminUserEntity getDataSerializable();

    public int update(long userId,String name);

    public int selectAndUpdate(String name);

    public int selectAndUpdate2(String name);

    public int selectAndUpdate3(String name);

    public long insert(String name);

    public long insert(long userId,String name);

}

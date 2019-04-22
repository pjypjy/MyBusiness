package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.products.restaurant.entity.UserInfoEntity;
import com.myBusiness.products.restaurant.dao.main.UserInfoMapper;
import com.myBusiness.products.restaurant.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-09-12
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Page<UserInfoEntity> page(int pageNo, int pageSize, String phone) {
        Page<UserInfoEntity> page = new Page<>();
        page.setSize(pageNo);
        page.setCurrent(pageSize);
        LambdaQueryWrapper<UserInfoEntity> lambda = new QueryWrapper<UserInfoEntity>().lambda();
        lambda.select(UserInfoEntity::getUserId);
        if(CommonUtil.isNotEmpty(phone)){
            lambda.eq(UserInfoEntity::getPhone,phone);
        }
        page.setTotal(userInfoMapper.selectCount(lambda));
        userInfoMapper.selectPage(page,lambda);
        return page;
    }
}

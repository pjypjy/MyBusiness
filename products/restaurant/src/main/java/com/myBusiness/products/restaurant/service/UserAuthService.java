package com.myBusiness.products.restaurant.service;

import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.entity.UserAuthEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-09-17
 */
public interface UserAuthService extends IService<UserAuthEntity> {

    /**
     * 1.用户如果是第一次登录，初始化一条 用户信息 和 用户认证信息
     * 2.如果是第二次登录，更新token、sessionKey
     *
     * 是否第一次登录，用openId是否存在去判断
     *
     * @param code
     * @return
     */
    public ResponseResult weChatLogin(String code);

    /**
     * 管理后台登录
     * @param loginId
     * @param password
     * @return
     */
    public ResponseResult adminWebLogin(String loginId,String password);
}

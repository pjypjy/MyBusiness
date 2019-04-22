package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.config.token.TokenManager;
import com.myBusiness.products.restaurant.config.token.UserToken;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.common.util.IdUtil;
import com.myBusiness.products.restaurant.config.WeChatConfig;
import com.myBusiness.products.restaurant.dao.main.*;
import com.myBusiness.products.restaurant.entity.AdminUserEntity;
import com.myBusiness.products.restaurant.entity.MenuBarEntity;
import com.myBusiness.products.restaurant.entity.UserAuthEntity;
import com.myBusiness.products.restaurant.entity.UserInfoEntity;
import com.myBusiness.products.restaurant.service.UserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-09-17
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuthEntity> implements UserAuthService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AdminUserMapper adminUserMapper;

    @Autowired
    UserAuthMapper userAuthMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WeChatConfig weChatConfig;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MenuBarMapper menuBarMapper;

    public Map getWeChatInfoFromWeChatServer(String code){
        Map<String, Object> params = new HashMap<>();
        params.put("appid", weChatConfig.getAppId());
        params.put("secret", weChatConfig.getAppSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String weChatServerUrl = "https://api.weixin.qq.com/sns/jscode2session";
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(weChatServerUrl, String.class, params);
        }catch (Exception e){
            logger.error("请求微信接口失败",e);
            return null;
        }
        HttpStatus statusCode = responseEntity.getStatusCode();
        String body = responseEntity.getBody();
        logger.debug("请求微信授权接口返回 , responseCode:{},responseBody:{}",statusCode.value(),body);
        Map resultFromWeChatServer = CommonUtil.json2Map(body);
        Object errCodeObj = resultFromWeChatServer.get("errcode");
        if(errCodeObj != null && statusCode.is2xxSuccessful() && "0".equals(String.valueOf(errCodeObj))){
            logger.debug("请求微信授权接口成功");
            return resultFromWeChatServer;
        }else{
            logger.debug("请求微信授权接口失败");
            return null;
        }
    }

    /**
     * 根据微信openId查询用户认证信息
     * @param openId
     * @return
     */
    public UserAuthEntity selectByOpenId(String openId){
        UserAuthEntity userAuthEntity = userAuthMapper.selectOne(new QueryWrapper<UserAuthEntity>().lambda().eq(UserAuthEntity::getWeChatOpenId, openId));
        return userAuthEntity;
    }

    /**
     * 1.用户如果是第一次登录，初始化一条 用户信息 和 用户认证信息
     * 2.如果是第二次登录，更新token、sessionKey
     * 是否第一次登录，用openId是否存在去判断
     * @param code
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class,value = "mainDatasourceTransactionManager")
    public ResponseResult weChatLogin(String code){
        Map weChatInfo = getWeChatInfoFromWeChatServer(code);
        if(weChatInfo == null){
            return ResponseResult.error("登录失败");
        }
        String openId = String.valueOf(weChatInfo.get("openid"));
        String sessionKey = String.valueOf(weChatInfo.get("session_key"));
        UserAuthEntity userAuthEntity = selectByOpenId(openId);
        String token = CommonUtil.uuid();
        if(userAuthEntity == null){
            long user_id = IdUtil.genUserId();

            userAuthEntity = new UserAuthEntity();
            userAuthEntity.setUserId(user_id);
            userAuthEntity.setToken(token);
            userAuthEntity.setWeChatOpenId(openId);
            userAuthEntity.setWeChatSessionKey(sessionKey);
            userAuthMapper.insert(userAuthEntity);

            UserInfoEntity userInfoEntity = new UserInfoEntity();
            userInfoEntity.setUserId(user_id);
            userInfoMapper.insert(userInfoEntity);
        }else{
            //把上一个token从redis删掉
            String oldToken = userAuthEntity.getToken();
            userAuthEntity.setToken(token);
            userAuthEntity.setWeChatSessionKey(sessionKey);
            userAuthMapper.updateById(userAuthEntity);
            tokenManager.delete(oldToken);
        }
        UserInfoEntity userInfoEntity = userInfoMapper.selectOne(new QueryWrapper<UserInfoEntity>()
                .lambda()
                .select(UserInfoEntity::getUserId)
                .eq(UserInfoEntity::getUserId, userAuthEntity.getUserId()));
        tokenManager.put(token, initTokenForWeChat(userAuthEntity.getUserId(),userInfoEntity.getName(),false));
        return ResponseResult.success(token);
    }

    @Override
    public ResponseResult adminWebLogin(String loginId, String password) {
        AdminUserEntity adminUserEntity = adminUserMapper.selectOne(new QueryWrapper<AdminUserEntity>()
                .lambda()
                .eq(AdminUserEntity::getLoginId, loginId)
                .select(AdminUserEntity::getUserId,AdminUserEntity::getLocalPassword,AdminUserEntity::getPasswordSuffix,AdminUserEntity::getToken));
        if(adminUserEntity == null){
            return ResponseResult.error("用户不存在");
        }
        String passwordSuffix = adminUserEntity.getPasswordSuffix();
        password = CommonUtil.encodeMD5(password.concat(passwordSuffix));

        String realPasswordMd5 = adminUserEntity.getLocalPassword();

        if(!realPasswordMd5.equals(password)){
            return ResponseResult.error("密码错误");
        }
        String token = CommonUtil.uuid();
        //把上一个token从redis删掉
        tokenManager.delete(adminUserEntity.getToken());

        //更新token
        adminUserMapper.update(new AdminUserEntity(), new UpdateWrapper<AdminUserEntity>()
                .lambda()
                .set(AdminUserEntity::getToken, token)
                .eq(AdminUserEntity::getUserId,adminUserEntity.getUserId()));

        tokenManager.put(token, initTokenForWeChat(adminUserEntity,true));
        return ResponseResult.success(token);
    }

    public UserToken initTokenForWeChat(AdminUserEntity adminUserEntity, boolean admin){
        UserToken userToken = new UserToken();
        userToken.setUserId(adminUserEntity.getUserId());
        userToken.setName(adminUserEntity.getName());
        Long roleId = adminUserEntity.getRoleId();
        if(roleId != null && admin){
            userToken.setRole(roleId);
            List<MenuBarEntity> menuBars = menuBarMapper.findMenuBarByRoleId(roleId);
            userToken.setMenuBars(menuBars);
        }
        userToken.setAdmin(admin);
        return userToken;
    }

    /**
     * 微信端登录，初始化token，无需生成对应角色的菜单，因为登录的都是客户(非管理员)
     * @param userId
     * @param name
     * @param admin
     * @return
     */
    public UserToken initTokenForWeChat(long userId, String name, boolean admin){
        UserToken userToken = new UserToken();
        userToken.setUserId(userId);
        userToken.setName(name);
        userToken.setAdmin(admin);
        return userToken;
    }
}

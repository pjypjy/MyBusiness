package com.myBusiness.products.restaurant.web;

import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.config.token.NoToken;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.common.util.StringPool;
import com.myBusiness.products.restaurant.service.UserAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Api(description = "登录/注册")
@RestController
@RequestMapping(value = "/login")
@Validated
public class LoginController {

    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserAuthService userAuthService;

    /**
     * 微信小程序登录逻辑
     *
     * 1.检查token是否存在，存在，则可以访问小程序则到4，失效或不存在进入2
     *
     * 2.微信api检查用户是否授权获取用户信息，未授权-弹框，已授权-进入3
     *      2.1 未授权弹框：允许-进入3，拒绝-不允许进行其他操作
     *      2.2 如果未授权-弹框-允许，则需要重新保存用户信息(flag = true)
     *
     * 3.wx.login得到code传到后台，成功获取到openid和session_key，返回token
     *      3.1 flag == true ? 调用wx.getUserInfo保存用户信息(saveOrUpdate)
     *
     * 4.所有请求携带token
     *
     * 5.token失效或不存在，重新wx.login(进入2)，续租token，更新session_key
     *
     * @param code
     * @return
     */
    @NoToken
    @ApiOperation(value = "登录并获取token" ,httpMethod = "POST")
    @RequestMapping(value = "/{clientType}",method = RequestMethod.POST)
    public ResponseResult getToken(
            @ApiParam(name="code",value = "临时登录凭证code(微信登录必传)",required = false)
            @RequestParam(required = false) String code,

            @NotBlank(message = "登录终端类型不能为空")
            @ApiParam(name="clientType",allowableValues = "adminWeb,weChat",value = "登录终端类型(必传)",required = true)
            @PathVariable(required = true) String clientType,

            @Size(min = 3,max=16,message = "登录账号长度必须为3~16个字符")
            @ApiParam(name="loginId",value = "登录账号(web登录必传)",required = false)
            @RequestParam(required = false) String loginId,

            @Size(min = 6,max = 24,message = "密码长度必须为6~24个字符")
            @ApiParam(name="password",value = "密码(web登录必传)",required = false)
            @RequestParam(required = false) String password){

        if(CommonUtil.isNotEmpty(password) && password.length() > 24){
            return ResponseResult.error("密码长度过长");
        }
        logger.debug("clientType:{},code:{}",clientType,code);
        if(StringPool.WE_CHAT.equals(clientType)){
            if(CommonUtil.isEmpty(code)){
                return ResponseResult.parameterLack();
            }
            return userAuthService.weChatLogin(code);
        }else if(StringPool.ADMIN_WEB.equals(clientType)){
            if(CommonUtil.isEmpty(loginId) || CommonUtil.isEmpty(password)){
                return ResponseResult.parameterLack();
            }
            return userAuthService.adminWebLogin(loginId,password);
        }else {
            return ResponseResult.parameterIllegal();
        }
    }

}

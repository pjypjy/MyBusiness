package com.myBusiness.products.restaurant.config.interceptor;

import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.config.token.NoToken;
import com.myBusiness.products.restaurant.config.token.TokenManager;
import com.myBusiness.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(NoToken.class)) {
            return true;
        }
        String token = request.getHeader("token");
        if(CommonUtil.isEmpty(token)){
            logger.debug("token不存在，请求被拒绝");
            CommonUtil.returnJsonResponse(ResponseResult.tokenExpire(),response);
            return false;
        }
        boolean available = tokenManager.isAvailable(token);
        if(!available){
            logger.debug("token过期，请求被拒绝");
            CommonUtil.returnJsonResponse(ResponseResult.tokenExpire(),response);
            return false;
        }
        tokenManager.refreshExpireDate(token);
        return true;
    }
}

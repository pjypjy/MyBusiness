package com.myBusiness.products.restaurant.config;

import com.myBusiness.common.exception.TokenExpireException;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.products.restaurant.config.token.TokenManager;
import com.myBusiness.products.restaurant.config.token.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {

    private static final int EXPIRE_MINUTES = 120 ;

    private static final String TOKEN_PREFIX = "TOKEN";

    @Autowired
    RedisTemplate<String,UserToken> redisTemplate;

    @Override
    public void put(String token,UserToken userToken){
        redisTemplate.opsForValue().set(TOKEN_PREFIX.concat(token),userToken, EXPIRE_MINUTES,TimeUnit.MINUTES);
    }

    @Override
    public UserToken get() {
        String tokenString = CommonUtil.getTokenString();
        if(CommonUtil.isEmpty(tokenString)){
            throw new TokenExpireException(tokenString);
        }
        return get(tokenString);
    }

    @Override
    public UserToken get(String token){
        return redisTemplate.opsForValue().get(TOKEN_PREFIX.concat(token));
    }

    @Override
    public long getUserId(String token) {
        UserToken userToken = get(token);
        if(userToken == null){
            throw new TokenExpireException(token);
        }else{
            return userToken.getUserId();
        }
    }

    @Override
    public long getUserId() {
        String tokenString = CommonUtil.getTokenString();
        if(CommonUtil.isEmpty(tokenString)){
            throw new TokenExpireException(tokenString);
        }
        UserToken userToken = get(tokenString);
        if(userToken == null){
            throw new TokenExpireException(tokenString);
        }else{
            return userToken.getUserId();
        }
    }

    @Override
    public boolean isAvailable(String token) {
        Boolean flag = redisTemplate.hasKey(TOKEN_PREFIX.concat(token));
        if(Boolean.FALSE.equals(flag)){
            return false;
        }
        return true;
    }

    @Override
    public void delete(String token) {
        redisTemplate.delete(TOKEN_PREFIX.concat(token));
    }

    @Override
    public void refreshExpireDate(String token) {
        redisTemplate.expire(TOKEN_PREFIX.concat(token),EXPIRE_MINUTES,TimeUnit.MINUTES);
    }


}

package com.myBusiness.products.restaurant.database;

import com.myBusiness.products.restaurant.config.token.UserToken;
import com.myBusiness.products.restaurant.RestaurantApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = RestaurantApplication.class)
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate<String, UserToken> myTemplate;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("test","aaa");
        Object test = redisTemplate.opsForValue().get("test");
        Assert.assertNotNull(test);
        Assert.assertEquals("aaa",String.valueOf(test));
    }

    @Test
    public void testExpire(){
        Boolean ppppp = redisTemplate.expire("ppppp", 1, TimeUnit.MINUTES);
        System.out.println(ppppp);
        Boolean test = redisTemplate.expire("test", 1, TimeUnit.MINUTES);
        System.out.println(test);
    }



    @Test
    public void testToken() throws InterruptedException {
        UserToken userToken = new UserToken();
        myTemplate.opsForValue().set("bb",userToken,1,TimeUnit.SECONDS);

        UserToken bb = myTemplate.opsForValue().get("bb");

        Thread.sleep(3 * 1000);

        bb = myTemplate.opsForValue().get("bb");
        System.out.println(bb);

    }

}

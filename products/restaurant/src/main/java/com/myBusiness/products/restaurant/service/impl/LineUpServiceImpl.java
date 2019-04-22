package com.myBusiness.products.restaurant.service.impl;

import com.myBusiness.common.util.StringPool;
import com.myBusiness.products.restaurant.service.LineUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Set;

@Service
public class LineUpServiceImpl implements LineUpService {

    private static final int LINE_UP_INIT_NUM = 168;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
     * 增加一个排队的人
     * @param userId 用户id
     * @return 成功/失败
     */
    @Override
    public Long inLineUp(long userId){
        Long size = redisTemplate.opsForZSet().zCard(StringPool.LINE_UP_KEY);
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(StringPool.LINE_UP_KEY, 0, 0);
        Long num ;
        if(typedTuples == null || typedTuples.size() == 0){
            num = (long) LINE_UP_INIT_NUM;
        }else{
            Double score = typedTuples.iterator().next().getScore();
            Integer scoreInt = score.intValue();
            num = (long) (++ scoreInt);
        }
        redisTemplate.opsForZSet().add(StringPool.LINE_UP_KEY,String.valueOf(userId),num);
        return num;
    }

    /**
     * 将序号最小的人拿出来
     * @return null-取不到人，否则返回userId
     */
    @Override
    public Long outLineUp(){
        Long next = next();
        if(next == null){
            return null;
        }
        redisTemplate.opsForZSet().remove(StringPool.LINE_UP_KEY,next);
        return next;
    }

    @Override
    public void resetLineUp() {
        redisTemplate.delete(StringPool.LINE_UP_KEY);
    }

    @Override
    public Long next() {
        Set<String> range = redisTemplate.opsForZSet().range(StringPool.LINE_UP_KEY, 0, 0);
        if(range == null){
            return null;
        }
        Iterator<String> iterator = range.iterator();
        String next = iterator.next();
        return next != null ? Long.parseLong(next) : null;
    }
}

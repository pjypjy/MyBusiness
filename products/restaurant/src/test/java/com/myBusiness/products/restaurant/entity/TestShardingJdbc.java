package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myBusiness.products.restaurant.RestaurantApplication;
import com.myBusiness.products.restaurant.service.SaleCommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = RestaurantApplication.class)
@RunWith(SpringRunner.class)
public class TestShardingJdbc {

    @Autowired
    SaleCommentService saleCommentService;

    @Test
    public void test(){
        SaleCommentEntity entity1 = new SaleCommentEntity();
        entity1.setGoodsId(1);
        entity1.setComment("很好啊");
        entity1.setOrderId(11L);
        entity1.setScore(5);
        entity1.setUserId(111L);
        entity1.setCommentDate(LocalDate.now());

        SaleCommentEntity entity2 = new SaleCommentEntity();
        entity2.setGoodsId(2);
        entity2.setComment("很好啊2");
        entity2.setOrderId(22L);
        entity2.setScore(4);
        entity2.setUserId(222L);
        entity2.setCommentDate(LocalDate.now());

        SaleCommentEntity entity3 = new SaleCommentEntity();
        entity3.setGoodsId(3);
        entity3.setComment("很好啊3");
        entity3.setOrderId(33L);
        entity3.setScore(3);
        entity3.setUserId(333L);
        entity3.setCommentDate(LocalDate.now());

        saleCommentService.save(entity1);
        saleCommentService.save(entity2);
        saleCommentService.save(entity3);
    }

    @Test
    public void test1(){
        List<SaleCommentEntity> list = saleCommentService.list(
                new QueryWrapper<SaleCommentEntity>()
                        .lambda()
                        .orderByDesc(SaleCommentEntity::getCommentDate));
        for(SaleCommentEntity entity : list){
            System.out.println(entity);
        }
    }
}

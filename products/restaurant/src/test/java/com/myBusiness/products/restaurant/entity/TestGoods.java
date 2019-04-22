package com.myBusiness.products.restaurant.entity;

import com.myBusiness.products.restaurant.RestaurantApplication;
import com.myBusiness.products.restaurant.config.FilePathConfig;
import com.myBusiness.products.restaurant.dao.main.GoodsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RestaurantApplication.class)
@RunWith(SpringRunner.class)
public class TestGoods {

    @Autowired
    GoodsMapper goodsMapper;

//    @Autowired
    FilePathConfig imgDataConfig;

    @Test
    public void watchPath(){
        System.out.println(imgDataConfig.getGoodsImgBaseLocalPath());
    }

    @Test
    public void testRename(){
        String name = "2.txt";
        System.out.println(name.substring(name.lastIndexOf(".")));
    }

    @Test
    public void testDysql(){
        goodsMapper.testDySql(1,2);
        goodsMapper.testDySql(2,-2);
    }

}

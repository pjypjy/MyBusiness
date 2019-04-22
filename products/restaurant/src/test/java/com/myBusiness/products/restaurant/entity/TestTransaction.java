package com.myBusiness.products.restaurant.entity;

import com.myBusiness.products.restaurant.RestaurantApplication;
import com.myBusiness.products.restaurant.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest(classes = RestaurantApplication.class)
@RunWith(SpringRunner.class)
public class TestTransaction {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GoodsService goodsService;

    @Test
    public void testTransaction(){
        goodsService.testTransaction();
    }

    @Test
    public void testTableExists(){
        String sql = "select 1 from information_schema.TABLES where table_name ='core_config' ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println(list);
    }
}

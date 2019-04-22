package com.myBusiness.products.restaurant.entity;

import com.myBusiness.products.restaurant.RestaurantApplication;
import com.myBusiness.products.restaurant.service.LineUpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RestaurantApplication.class)
@RunWith(SpringRunner.class)
public class TestLineUp {

    @Autowired
    LineUpService lineUpService;

    @Test
    public void test(){
//        lineUpService.inLineUp(123);
        lineUpService.inLineUp(634);
        lineUpService.inLineUp(335);
        lineUpService.inLineUp(565);
        lineUpService.inLineUp(224);
        Long aLong = lineUpService.outLineUp();
        System.out.println(aLong);
    }

    @Test
    public void testNext(){
        Long next = lineUpService.next();
        System.out.println(next);
    }

}

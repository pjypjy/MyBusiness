package com.myBusiness.products.restaurant.entity;

import com.myBusiness.products.restaurant.RestaurantApplication;
import com.myBusiness.products.restaurant.dao.main.SaleSituationDetailMapper;
import com.myBusiness.products.restaurant.service.SaleSituationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@SpringBootTest(classes = RestaurantApplication.class)
@RunWith(SpringRunner.class)
public class TestSaleSituation {

    @Autowired
    SaleSituationDetailMapper saleSituationDetailMapper;

    @Test
    public void test1(){
        SaleSituationService.SituationObj obj = new SaleSituationService.SituationObj(1,LocalDate.now(),1,5);
        int i = saleSituationDetailMapper.updateSituation(obj);
        System.out.println(i);
    }
}

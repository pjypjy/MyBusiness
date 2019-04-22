package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.products.restaurant.RestaurantApplication;
import com.myBusiness.products.restaurant.service.MyMenuService;
import com.myBusiness.products.restaurant.vo.BaseGoodsInfoVo;
import com.myBusiness.products.restaurant.vo.MyMenuVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = RestaurantApplication.class)
@RunWith(SpringRunner.class)
public class TestMyMenu {

    @Autowired
    MyMenuService myMenuService;

    @Test
    public void testInsertJson(){

        BaseGoodsInfoVo orderGoodsInfo = new BaseGoodsInfoVo();
        orderGoodsInfo.setGoodsId(3);
        orderGoodsInfo.setAmount(20);

        List<BaseGoodsInfoVo> list = new ArrayList<>();
        list.add(orderGoodsInfo);

        MyMenuEntity myMenuEntity = new MyMenuEntity();
        myMenuEntity.setUserId(1L);
        myMenuEntity.setMenuName("456");

        myMenuEntity.setGoodsInfo(CommonUtil.object2JsonString(list));
        myMenuService.insertOrUpdate(myMenuEntity);
    }

    @Test
    public void test1(){
        MyMenuVo goodsInfoView = myMenuService.getMyMenuVo(1, 1);
        System.out.println(goodsInfoView.getMenuName());
        MyMenuEntity myMenuEntity = new MyMenuEntity();
        BeanUtils.copyProperties(goodsInfoView,myMenuEntity);
        System.out.println(myMenuEntity.getMenuName());
    }
}

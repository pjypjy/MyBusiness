package com.myBusiness.products.restaurant.entity;

import com.myBusiness.products.restaurant.RestaurantApplication;
import com.myBusiness.products.restaurant.dao.main.MenuBarForRoleMapper;
import com.myBusiness.products.restaurant.dao.main.MenuBarMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = RestaurantApplication.class)
@RunWith(SpringRunner.class)
public class TestAuthority {

    @Autowired
    MenuBarMapper menuBarMapper;

    @Autowired
    MenuBarForRoleMapper menuBarForRoleMapper;

    @Test
    public void testMenuForRole(){
        List<MenuBarEntity> simpleMenuBar = menuBarMapper.findMenuBarByRoleId(1L);
        for(MenuBarEntity entity : simpleMenuBar){
            System.out.println("=========");
            System.out.println(entity.getName());
            List<MenuBarEntity> sonMenuBars = entity.getSonMenuBars();
            if( simpleMenuBar.size() == 0){
                System.out.println("empty");
            }else{
                if(sonMenuBars!=null){
                    for(MenuBarEntity se : sonMenuBars){
                        if(se != null){
                            System.out.println("SE:"+se.getName());
                        }else{
                            System.out.println("SE:empty");
                        }

                    }
                }else{
                    System.out.println("son:empty");
                }

            }
        }
    }

    @Test
    public void testMultiId(){
        Boolean exists = menuBarForRoleMapper.exists(1L, 2L);
        System.out.println(exists);
    }
}

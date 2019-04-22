package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.RestaurantApplication;
import com.myBusiness.products.restaurant.config.WeChatConfig;
import com.myBusiness.products.restaurant.dao.main.AdminUserMapper;
import com.myBusiness.products.restaurant.dao.main.UserAuthMapper;
import com.myBusiness.products.restaurant.dao.main.UserInfoMapper;
import com.myBusiness.products.restaurant.service.UserInfoService;
import com.myBusiness.products.restaurant.service.impl.UserAuthServiceImpl;
import com.myBusiness.products.restaurant.service.impl.UserInfoServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RestaurantApplication.class)
@RunWith(SpringRunner.class)
public class UserInfoEntityTest {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserAuthMapper userAuthMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserInfoServiceImpl userInfoServiceImpl;

    @Autowired
    UserAuthServiceImpl userAuthServiceImpl;

    @Autowired
    WeChatConfig weChatConfig;

    @Test
    public void testInsertUserInfo(){
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUserId(1L);
        userInfoService.save(userInfoEntity);
    }

    @Test
    public void testInsertUserAuth(){
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUserId(1L);
        userAuthEntity.setWeChatOpenId("444");
        userAuthEntity.setWeChatSessionKey("444");
        userAuthMapper.insert(userAuthEntity);
    }

    @Test
    public void testFindById(){
        UserInfoEntity userInfoEntity = userInfoMapper.selectById(1L);
        Assert.assertNotNull(userInfoEntity);
        UserInfoEntity byId = userInfoService.getById(1L);
        Assert.assertNotNull(byId);

        UserInfoEntity entity = userInfoMapper.selectOne(new QueryWrapper<UserInfoEntity>().lambda().eq(UserInfoEntity::getAllPoints, 0));
        System.out.println(entity);
    }

    @Test
    public void testUpdateUserAuth(){
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUserId(1L);
        userAuthEntity.setToken("123");
        userAuthEntity.setWeChatOpenId("123");
        userAuthEntity.setWeChatSessionKey("123");
        int i = userAuthMapper.updateById(userAuthEntity);
        Assert.assertEquals(1,i);
    }

    @Test
    public void saveOrUpdate(){
        ResponseResult responseResult = userAuthServiceImpl.weChatLogin("");
        System.out.println(responseResult);
    }

    @Test
    public void testQueryCustomColumn(){
        ResponseResult responseResult = userAuthServiceImpl.adminWebLogin("10", "3");

    }

    /**
     * update方法默认跳过null属性的更新，
     * 如果要更新null属性，目前只能使用UpdateWrapper
     */
    @Test
    public void testUpdateWarp(){
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setName("777");
//        userInfoEntity.setSex("0001-1");
//        userInfoEntity.setUserId(111L);
        int update = userInfoMapper.update(null,
                new UpdateWrapper<UserInfoEntity>()
                        .lambda()
                        .eq(UserInfoEntity::getUserId, 111L)
                        .set(UserInfoEntity::getName,"888"));
        System.out.println(update);
    }

    @Test
    public void testUpdateNull(){
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setName("");
        userInfoEntity.setSex("0001-3");
        userInfoEntity.setUserId(111L);
//        int update = userInfoMapper.(userInfoEntity);
//        System.out.println(update);
    }

    @Autowired
    AdminUserMapper adminUserMapper;

    @Test
    public void testPage(){
        Page<AdminUserEntity> page = new Page<>();
        LambdaQueryWrapper<AdminUserEntity> query = new QueryWrapper<AdminUserEntity>()
                .lambda()
                .select(AdminUserEntity::getLoginId)
                .eq(AdminUserEntity::getSex, "0001-3");
        for(int i = 1 ; i <= 6 ; i ++){
            page.setSize(3);
            page.setCurrent(i);
            page.setTotal(adminUserMapper.selectCount(query));
            IPage<AdminUserEntity> adminUserEntityIPage = adminUserMapper.selectPage(page, query);
            System.out.println("第"+page.getCurrent()+"页"+",总数："+page.getTotal());
            for(AdminUserEntity entity : page.getRecords()){
                System.out.println(entity.getLoginId()+","+entity.getName());
            }
        }

    }

    @Test
    public void testMyPage(){
        Boolean b = true;
        System.out.println(Boolean.TRUE.equals(b));
    }

    @Test
    public void testExists(){
        System.out.println(adminUserMapper.exists(5L));
        System.out.println(adminUserMapper.exists(77L));
    }
}

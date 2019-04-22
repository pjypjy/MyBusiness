package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.exception.UserNotFoundException;
import com.myBusiness.products.restaurant.entity.AdminUserEntity;
import com.myBusiness.products.restaurant.dao.main.AdminUserMapper;
import com.myBusiness.products.restaurant.service.AdminUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-09-17
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserEntity> implements AdminUserService {

    protected final static Logger logger = LoggerFactory.getLogger(AdminUserServiceImpl.class);

    @Autowired
    AdminUserMapper adminUserMapper;

    @Autowired
    @Qualifier(value = "mainJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Override
    public String getToken(long userId) throws UserNotFoundException {
        AdminUserEntity adminUserEntity = adminUserMapper.selectOne(new QueryWrapper<AdminUserEntity>()
                .lambda()
                .eq(AdminUserEntity::getUserId, userId)
                .select(AdminUserEntity::getToken));
        if (adminUserEntity == null) {
            throw new UserNotFoundException(userId);
        }
        return adminUserEntity.getToken();
    }

    @Override
    public int enableOrDisableUser(long userId, boolean enable) {
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setUserId(userId);
        adminUserEntity.setEnable(enable);
        return adminUserMapper.updateById(adminUserEntity);
    }

    @Override
    public AdminUserEntity getById(long userId) {
        AdminUserEntity adminUserEntity = adminUserMapper.selectById(userId);
        adminUserEntity.setLocalPassword(null);
        adminUserEntity.setPasswordSuffix(null);
        return adminUserEntity;
    }

    @Override
    public Page<AdminUserEntity> page(int pageNo, int pageSize, LambdaQueryWrapper<AdminUserEntity> lambda) {
        Page<AdminUserEntity> page = new Page<>();
        page.setSize(pageNo);
        page.setCurrent(pageSize);
        page.setTotal(adminUserMapper.selectCount(lambda));
        adminUserMapper.selectPage(page, lambda);
        return page;
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager",isolation = Isolation.READ_UNCOMMITTED)
    public AdminUserEntity getDataUnCommit(long userId) {
        AdminUserEntity adminUserEntity = adminUserMapper.selectById(userId);
        logger.info("getData [READ_UNCOMMITTED] => adminUserEntity:{}",adminUserEntity);
        return adminUserEntity;
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager",isolation = Isolation.READ_COMMITTED)
    public AdminUserEntity getDataCommitted() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from admin_user");
        logger.info("getData [READ_COMMITTED] (1) :");
        for(Map<String, Object> adminUserEntity : list){
            logger.info(adminUserEntity.toString());
        }
        try {
            Thread.sleep(1000 * 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list = jdbcTemplate.queryForList("select * from admin_user");
        logger.info("getData [READ_COMMITTED] (2) :");
        for(Map<String, Object> adminUserEntity : list){
            logger.info(adminUserEntity.toString());
        }
        return null;
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager",isolation = Isolation.REPEATABLE_READ)
    public AdminUserEntity getDataRepeatable() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from admin_user");
        logger.info("getData [REPEATABLE_READ] (1) :");
        for(Map<String, Object> adminUserEntity : list){
            logger.info(adminUserEntity.toString());
        }
        try {
            Thread.sleep(1000 * 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list = jdbcTemplate.queryForList("select * from admin_user");
        logger.info("getData [REPEATABLE_READ] (2) :");
        for(Map<String, Object> adminUserEntity : list){
            logger.info(adminUserEntity.toString());
        }
        return null;
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager",isolation = Isolation.SERIALIZABLE)
    public AdminUserEntity getDataSerializable() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from admin_user");
        logger.info("getData [SERIALIZABLE] (1) :");
        for(Map<String, Object> adminUserEntity : list){
            logger.info(adminUserEntity.toString());
        }
        try {
            Thread.sleep(1000 * 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list = jdbcTemplate.queryForList("select * from admin_user");
        logger.info("getData [SERIALIZABLE] (2) :");
        for(Map<String, Object> adminUserEntity : list){
            logger.info(adminUserEntity.toString());
        }
        return null;
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager")
    public int update(long userId,String name) {
        logger.info("----------  update start ----------");
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setUserId(userId);
        adminUserEntity.setName(name);
        logger.info("set name = '{}'",name);
        int i = adminUserMapper.updateById(adminUserEntity);
        logger.info("update sleeping");
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("----------  update sleep end  ----------");
        return i;
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager",isolation = Isolation.REPEATABLE_READ)
    public int selectAndUpdate(String name) {
        logger.info("----------  selectAndUpdate start ----------");
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setName(name);
        logger.info("set name = '{}'",name);

        //1.查询全表数量
        Integer size = adminUserMapper.selectCount(new QueryWrapper<AdminUserEntity>());
        logger.info("size1 :{}",size);
        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //2.全表更新 name字段
        int updateSize = adminUserMapper.update(new AdminUserEntity(),new UpdateWrapper<AdminUserEntity>().lambda().set(AdminUserEntity::getName,name));
        logger.info("selectAndUpdateSize :{}",updateSize);
        //3.查询全表数量
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from admin_user");
        logger.info("size2:{}",list.size());
        logger.info("----------  selectAndUpdate sleep end  ----------");
        return list.size();
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager",isolation = Isolation.REPEATABLE_READ)
    public int selectAndUpdate2(String name) {
        logger.info("----------  selectAndUpdate2 start ----------");
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setName(name);
        logger.info("set name = '{}'",name);

        //1.查询全表数量
        Integer size = adminUserMapper.selectCount(new QueryWrapper<AdminUserEntity>());
        logger.info("size1 :{}",size);

        //2.全表更新 name字段
        int updateSize = adminUserMapper.update(new AdminUserEntity(),new UpdateWrapper<AdminUserEntity>().lambda().set(AdminUserEntity::getName,name));
        logger.info("selectAndUpdateSize :{}",updateSize);

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //3.查询全表数量
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from admin_user");
        logger.info("size2:{}",list.size());
        logger.info("----------  selectAndUpdate2 sleep end  ----------");
        return list.size();
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager",isolation = Isolation.SERIALIZABLE)
    public int selectAndUpdate3(String name) {
        logger.info("----------  selectAndUpdate3 start ----------");
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setName(name);
        logger.info("set name = '{}'",name);

        //1.查询全表数量
        Integer size = adminUserMapper.selectCount(new QueryWrapper<AdminUserEntity>());
        logger.info("size1 :{}",size);

        //2.全表更新 name字段
        int updateSize = adminUserMapper.update(new AdminUserEntity(),new UpdateWrapper<AdminUserEntity>().lambda().set(AdminUserEntity::getName,name));
        logger.info("selectAndUpdateSize :{}",updateSize);

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //3.查询全表数量
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from admin_user");
        logger.info("size2:{}",list.size());
        logger.info("----------  selectAndUpdate3 sleep end  ----------");
        return list.size();
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager")
    public long insert(String name) {
        logger.info("----------  insert start ----------");
        long id = System.currentTimeMillis();
        logger.info("the id is : {}",id);
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setUserId(id);
        adminUserEntity.setName(name);
        adminUserEntity.setPhone("110");
        logger.info("set name = '{}'",name);
        int i = adminUserMapper.insert(adminUserEntity);
        logger.info("insert sleeping");
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("----------  insert sleep end  ----------");
        return id;
    }

    @Override
    public long insert(long userId, String name) {
        logger.info("----------  insert start ----------");
        logger.info("the id is : {}",userId);
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setUserId(userId);
        adminUserEntity.setName(name);
        adminUserEntity.setPhone("110");
        logger.info("set name = '{}'",name);
        int i = adminUserMapper.insert(adminUserEntity);
        logger.info("insert sleeping");
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("----------  insert sleep end  ----------");
        return userId;
    }

}

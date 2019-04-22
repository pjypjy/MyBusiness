package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.common.util.StringPool;
import com.myBusiness.products.restaurant.entity.GoodsEntity;
import com.myBusiness.products.restaurant.dao.main.GoodsMapper;
import com.myBusiness.products.restaurant.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-09-25
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, GoodsEntity> implements GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    public ResponseResult dealDiscountStatus(Integer goodsId, String type, BigDecimal discount) {
        int updateResult = 0;
        if(StringPool.ENABLE.equals(type)){
            if(discount == null){
                return ResponseResult.error("请输入折扣值");
            }
            if(discount.compareTo(new BigDecimal(9.99)) > 0){
                return ResponseResult.error("折扣值必须小于等于9.99");
            }
            updateResult = goodsMapper.update(new GoodsEntity(), new UpdateWrapper<GoodsEntity>()
                    .lambda()
                    .set(GoodsEntity::getEnableDiscount, true)
                    .set(GoodsEntity::getDiscount, discount)
                    .eq(GoodsEntity::getGoodsId, goodsId));
        }else{
            updateResult = goodsMapper.update(new GoodsEntity(),new UpdateWrapper<GoodsEntity>()
                    .lambda()
                    .set(GoodsEntity::getEnableDiscount,false)
                    .set(GoodsEntity::getDiscount,null)
                    .eq(GoodsEntity::getGoodsId,goodsId));
        }
        return updateResult > 0 ? ResponseResult.success() : ResponseResult.error("更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class,value = "mainDatasourceTransactionManager")
    public ResponseResult changeAmount(int goodsId, int changeAmount) {
        return goodsMapper.changeAmount(goodsId,changeAmount) > 0 ? ResponseResult.success() : ResponseResult.error("库存数量不足");
    }

    @Override
    public ResponseResult dealAmountStatus(int goodsId, String type) {
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoodsId(goodsId);
        if(StringPool.ENABLE.equals(type)){
            goodsEntity.setEnableAmount(true);
        }else{
            goodsEntity.setEnableAmount(false);
        }
        return goodsMapper.updateById(goodsEntity) > 0 ? ResponseResult.success() : ResponseResult.error("更新失败");
    }

    @Override
    public ResponseResult dealGoodsStatus(int goodsId, String type) {
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoodsId(goodsId);
        if(StringPool.ENABLE.equals(type)){
            goodsEntity.setEnable(true);
        }else{
            goodsEntity.setEnable(false);
        }
        return goodsMapper.updateById(goodsEntity) > 0 ? ResponseResult.success() : ResponseResult.error("更新失败");
    }

    @Override
    public Page<GoodsEntity> pageQuery(int pageNo, int pageSize, Integer categoryId, String name) {
        Page<GoodsEntity> page = new Page<>();
        page.setSize(pageNo);
        page.setCurrent(pageSize);
        LambdaQueryWrapper<GoodsEntity> lambda = new QueryWrapper<GoodsEntity>().lambda();
        lambda.select(GoodsEntity::getGoodsId,
                GoodsEntity::getEnableAmount,
                GoodsEntity::getAmount,
                GoodsEntity::getImg,
                GoodsEntity::getName,
                GoodsEntity::getPrice,
                GoodsEntity::getEnableDiscount,
                GoodsEntity::getDiscount);
        if(categoryId != null){
            lambda.eq(GoodsEntity::getCategoryId,categoryId);
        }
        if(CommonUtil.isNotEmpty(name)){
            lambda.like(GoodsEntity::getName,"%"+name+"%");
        }
        page.setTotal(goodsMapper.selectCount(lambda));
        goodsMapper.selectPage(page,lambda);
        return page;
    }

    @Override
    @Transactional(value = "mainDatasourceTransactionManager")
    public void testTransaction(){
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoodsId(100);
        goodsEntity.setAmount(1);
        goodsEntity.setPrice(new BigDecimal(100));
        goodsEntity.setName("test");
        goodsEntity.setCategoryId(789);
        goodsEntity.setImg("");
        goodsMapper.insert(goodsEntity);
//        throw new RuntimeException();
    }

}

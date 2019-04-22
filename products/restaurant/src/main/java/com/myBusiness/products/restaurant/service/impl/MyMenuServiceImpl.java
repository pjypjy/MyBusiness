package com.myBusiness.products.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.products.restaurant.entity.MyMenuEntity;
import com.myBusiness.products.restaurant.dao.main.MyMenuMapper;
import com.myBusiness.products.restaurant.service.MyMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myBusiness.products.restaurant.vo.GoodsInfoVo;
import com.myBusiness.products.restaurant.vo.MyMenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pjy
 * @since 2018-09-27
 */
@Service
public class MyMenuServiceImpl extends ServiceImpl<MyMenuMapper, MyMenuEntity> implements MyMenuService {

    @Autowired
    MyMenuMapper myMenuMapper;

    @Override
    public boolean insertOrUpdate(MyMenuEntity myMenuEntity) {
        if(CommonUtil.isEmpty(myMenuEntity.getMenuName())){
            myMenuEntity.setMenuName("我的菜单" + myMenuEntity.getIndex());
        }
        if(myMenuEntity.getIndex() == null){
            return myMenuMapper.insert(myMenuEntity) > 0;
        }else{
            myMenuEntity.setLastUpdateDate(LocalDateTime.now());
            return myMenuMapper.update(myMenuEntity, new UpdateWrapper<MyMenuEntity>()
                    .lambda()
                    .set(MyMenuEntity::getGoodsInfo,myMenuEntity.getGoodsInfo())
                    .eq(MyMenuEntity::getUserId, myMenuEntity.getUserId())
                    .eq(MyMenuEntity::getIndex, myMenuEntity.getIndex())) > 0;
        }
    }

    @Override
    public MyMenuVo getMyMenuVo(long userId, int index) {
        MyMenuEntity myMenuEntity = myMenuMapper.selectOne(new QueryWrapper<MyMenuEntity>()
                .lambda()
                .select(MyMenuEntity::getGoodsInfo)
                .select(MyMenuEntity::getMenuName)
                .select(MyMenuEntity::getUserId)
                .select(MyMenuEntity::getIndex)
                .eq(MyMenuEntity::getUserId, userId)
                .eq(MyMenuEntity::getIndex, index));
        String goodsInfoJsonString = myMenuEntity.getGoodsInfo();
        List<GoodsInfoVo> list = CommonUtil.jsonString2Object(goodsInfoJsonString, List.class);
        MyMenuVo vo = new MyMenuVo();
        BeanUtils.copyProperties(myMenuEntity,vo);
        vo.setGoodsInfos(list);
        return vo;
    }
}

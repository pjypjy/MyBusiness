package com.myBusiness.products.restaurant.dao.main;

import com.myBusiness.products.restaurant.entity.MyMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pjy
 * @since 2018-09-27
 */
public interface MyMenuMapper extends BaseMapper<MyMenuEntity> {

    public int insert(MyMenuEntity myMenuEntity);

}

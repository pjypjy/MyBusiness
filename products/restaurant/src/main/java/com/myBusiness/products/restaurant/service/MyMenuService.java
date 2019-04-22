package com.myBusiness.products.restaurant.service;

import com.myBusiness.products.restaurant.entity.MyMenuEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myBusiness.products.restaurant.vo.MyMenuVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pjy
 * @since 2018-09-27
 */
public interface MyMenuService extends IService<MyMenuEntity> {

    public boolean insertOrUpdate(MyMenuEntity myMenuEntity);

    public MyMenuVo getMyMenuVo(long userId, int index);
}

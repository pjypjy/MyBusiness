package com.myBusiness.products.restaurant.dao.main;

import com.myBusiness.products.restaurant.entity.SaleSituationSummaryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myBusiness.products.restaurant.service.SaleSituationService;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pjy
 * @since 2018-11-05
 */
public interface SaleSituationSummaryMapper extends BaseMapper<SaleSituationSummaryEntity> {

    /**
     * 支付回调成功，更新销量
     * @param obj
     * @return
     */
    public void updateSaleSituationSummary(SaleSituationService.SituationObj obj);

    /**
     * 增加评价
     * @param obj
     * @return
     */
    public int updateEvaluate(SaleSituationService.SituationObj obj);

}

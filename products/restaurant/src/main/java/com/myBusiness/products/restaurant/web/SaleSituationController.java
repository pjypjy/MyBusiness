package com.myBusiness.products.restaurant.web;


import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.entity.SaleCommentEntity;
import com.myBusiness.products.restaurant.service.SaleSituationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pjy
 * @since 2018-11-05
 */
@Api(description = "销量、评价管理")
@RestController
@RequestMapping("/saleSituation")
public class SaleSituationController {

    @Autowired
    SaleSituationService saleSituationService;

    @ApiOperation(value = "销量/评价 后台统计:只传一个时间表示查询当天，传两个时间表示查询区间内的销量，不传表示查询总销量" ,httpMethod = "GET")
    @RequestMapping(value = "/goods/{goodsId}",method = RequestMethod.GET)
    public ResponseResult getGoodsSaleSituation(
            @ApiParam(name="goodsId",value = "菜单id",required = true)
            @PathVariable(required = true) Integer goodsId,

            @ApiParam(name="startDate",value = "起始时间",required = false)
            @RequestParam(required = false) LocalDate startDate,

            @ApiParam(name="endDate",value = "结束时间",required = false)
            @RequestParam(required = false) LocalDate endDate){

        return saleSituationService.getGoodsSaleSituation(goodsId, startDate, endDate);
    }

    @ApiOperation(value = "评价订单中的商品" ,httpMethod = "PUT")
    @RequestMapping(value = "/evaluate/{orderId}/{goodsId}",method = RequestMethod.PUT)
    public ResponseResult evaluate(
            @ApiParam(name="orderId",value = "订单id",required = true)
            @PathVariable(required = true) Long orderId,

            @ApiParam(name="goodsId",value = "商品id",required = true)
            @PathVariable(required = true) Integer goodsId,

            @ApiParam(name="score",value = "分级",required = true)
            @RequestParam(required = true) Integer score,

            @ApiParam(name="comment",value = "评论",required = false)
            @RequestParam(required = false) String comment){

        try {
            return saleSituationService.evaluateGoodsInOrder(orderId,goodsId,score,comment);
        } catch (Exception e) {
            return ResponseResult.error("评价失败");
        }
    }

    @ApiOperation(value = "追评订单中的商品" ,httpMethod = "PUT")
    @RequestMapping(value = "/review/{orderId}/{goodsId}",method = RequestMethod.PUT)
    public ResponseResult review(
            @ApiParam(name="orderId",value = "订单id",required = true)
            @PathVariable(required = true) Long orderId,

            @ApiParam(name="goodsId",value = "商品id",required = true)
            @PathVariable(required = true) Integer goodsId,

            @ApiParam(name="review",value = "追评内容",required = true)
            @RequestParam(required = true) String review){

        try {
            return saleSituationService.reviewGoodsInOrder(orderId,goodsId,review);
        } catch (Exception e) {
            return ResponseResult.error("追评失败");
        }
    }

    @ApiOperation(value = "查询订单中的商品评价情况" ,httpMethod = "GET")
    @RequestMapping(value = "/evaluation/order/{orderId}",method = RequestMethod.GET)
    public ResponseResult queryEvaluateByOrderId(
            @ApiParam(name="goodsId",value = "订单id",required = true)
            @PathVariable(required = true) Long orderId){

        return saleSituationService.queryEvaluateByOrderId(orderId);
    }

    @ApiOperation(value = "查询某个商品评价情况(降序)" ,httpMethod = "GET")
    @RequestMapping(value = "/evaluation/goods/{goodsId}",method = RequestMethod.GET)
    public ResponseResult queryEvaluateByGoodsId(
            @ApiParam(name="goodsId",value = "商品id",required = true)
            @PathVariable(required = true) Integer goodsId,

            @ApiParam(name="maxOrderId",value = "最小订单id",required = true)
            @RequestParam(required = true) Long minOrderId,

            @Max(value = 20,message = "一次最多返回20条记录")
            @ApiParam(name="limit",value = "返回多少条",required = true)
            @RequestParam(required = true) Integer limit){

        List<SaleCommentEntity> list = saleSituationService.queryEvaluateByGoodsId(goodsId, minOrderId, limit);
        return ResponseResult.success(list);
    }
}

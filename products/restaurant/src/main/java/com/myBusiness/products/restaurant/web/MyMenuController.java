package com.myBusiness.products.restaurant.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.products.restaurant.config.token.TokenManager;
import com.myBusiness.products.restaurant.entity.MyMenuEntity;
import com.myBusiness.products.restaurant.service.MyMenuService;
import com.myBusiness.products.restaurant.vo.MyMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pjy
 * @since 2018-09-27
 */
@Api(description = "\"我的菜单\"管理")
@RestController
@RequestMapping("/myMenu")
public class MyMenuController {

    private final static Logger logger = LoggerFactory.getLogger(MyMenuController.class);

    @Autowired
    MyMenuService myMenuService;

    @Autowired
    TokenManager tokenManager;

    @ApiOperation(value = "新增/修改 我的菜单" ,httpMethod = "POST")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseResult saveMyMenu(
            @ApiParam(name="index",value = "菜单序号",required = false)
            @RequestParam(required = false) Integer index,

            @Size(max = 8,message = "菜单名称长度为2~10位字符")
            @ApiParam(name="name",value = "菜单名称",required = true)
            @RequestParam(required = true) String menuName,

            @ApiParam(name="goods_info",value = "菜单中的商品信息(json格式:{'goods_id':'1'}，商品id为key，数量为value)",required = true)
            @RequestParam(required = true) String goods_info){

        try {
            CommonUtil.jsonString2Object(goods_info, List.class);
        }catch (Exception e){
            logger.error("json格式错误:{}",goods_info);
            return ResponseResult.error("json格式错误");
        }

        MyMenuEntity myMenuEntity = new MyMenuEntity();
        myMenuEntity.setUserId(tokenManager.getUserId());
        myMenuEntity.setIndex(index);
        myMenuEntity.setGoodsInfo(goods_info);
        myMenuEntity.setMenuName(menuName);
        myMenuEntity.setGoodsInfo(goods_info);
        boolean b = myMenuService.insertOrUpdate(myMenuEntity);
        return b ? ResponseResult.success() : ResponseResult.error("保存失败");
    }

    @ApiOperation(value = "删除 我的菜单" ,httpMethod = "DELETE")
    @RequestMapping(value = "/delete/{index}",method = RequestMethod.DELETE)
    public ResponseResult saveMyMenu(
            @NotBlank(message = "菜单序号不能为空")
            @ApiParam(name="index",value = "菜单序号",required = false)
            @PathVariable(required = false) Integer index){

        boolean remove = myMenuService.remove(new QueryWrapper<MyMenuEntity>()
                .lambda()
                .eq(MyMenuEntity::getUserId, tokenManager.getUserId())
                .eq(MyMenuEntity::getIndex, index));
        return remove ? ResponseResult.success() : ResponseResult.error("删除失败");
    }

    @ApiOperation(value = "我的菜单 列表" ,httpMethod = "GET")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResponseResult list(){
        List<MyMenuEntity> myMenuEntityList = myMenuService.list(new QueryWrapper<MyMenuEntity>().lambda()
                .select(MyMenuEntity::getIndex)
                .select(MyMenuEntity::getMenuName)
                .eq(MyMenuEntity::getUserId, tokenManager.getUserId()));
        return ResponseResult.success(myMenuEntityList);

    }

    @ApiOperation(value = "菜单详情" ,httpMethod = "GET")
    @RequestMapping(value = "/get/{index}",method = RequestMethod.GET)
    public ResponseResult get(
            @NotBlank(message = "菜单序号不能为空")
            @ApiParam(name="index",value = "菜单序号",required = true)
            @PathVariable(required = false) Integer index){
        MyMenuVo goodsInfoView = myMenuService.getMyMenuVo(tokenManager.getUserId(), index);
        return ResponseResult.success(goodsInfoView);
    }

}

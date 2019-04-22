package com.myBusiness.products.restaurant.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.common.util.IdUtil;
import com.myBusiness.common.util.StringPool;
import com.myBusiness.products.restaurant.entity.MenuBarEntity;
import com.myBusiness.products.restaurant.service.MenuBarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Api(description = "菜单管理")
@RestController
@RequestMapping(value = "/menuBar")
public class MenuBarController {

    @Autowired
    MenuBarService menuBarService;

    @ApiOperation(value = "新增/修改 菜单" ,httpMethod = "POST")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseResult saveMenuBar(
            @ApiParam(name="menuBarId",value = "菜单id,不为null表示修改,否则表示新增",required = false)
            @RequestParam(required = false) Long menuBarId,

            @NotBlank(message = "菜单名称不能为空")
            @Size(min = 2,max = 8,message = "菜单名称长度为2~8位字符")
            @ApiParam(name="name",value = "菜单名称",required = true)
            @RequestParam(required = true) String name,

            @NotBlank(message = "菜单描述不能为空")
            @Size(max = 128,message = "菜单描述最大长度为128位字符")
            @ApiParam(name="description",value = "菜单描述",required = true)
            @RequestParam(required = true) String description,

            @NotBlank(message = "菜单url不能为空")
            @Size(max = 128,message = "菜单url最大长度为128位字符")
            @ApiParam(name="url",value = "菜单url",required = true)
            @RequestParam(required = true) String url,

            @NotBlank(message = "菜单顺序号不能为空")
            @ApiParam(name="order",value = "菜单顺序号",required = true)
            @RequestParam(required = true) int order,

            @ApiParam(name="parent",value = "父菜单id",required = false)
            @RequestParam(required = false) Long parent,

            @NotBlank(message = "请选择是否启用")
            @ApiParam(name="enable",value = "是否启用",required = true)
            @RequestParam(required = true) Boolean enable){

        if(parent != null){
            MenuBarEntity parentMenuBar = menuBarService.getById(parent);
            if(parentMenuBar == null){
                return ResponseResult.error("父菜单不存在，请重新选择对应的父菜单");
            }
        }

        MenuBarEntity menuBar = new MenuBarEntity();
        if(menuBarId == null){
            //新增
            menuBar.setMenuBarId(IdUtil.genMenuBarId());
            menuBar.setName(name);
            menuBar.setDescription(description);
            menuBar.setUrl(url);
            menuBar.setOrder(order);
            menuBar.setParent(parent);
            menuBar.setEnable(enable);
        }else{
            //修改
            menuBar = menuBarService.getById(menuBarId);
            if(menuBar == null){
                return ResponseResult.error("该菜单不存在");
            }
            menuBar.setName(name);
            menuBar.setDescription(description);
            menuBar.setUrl(url);
            menuBar.setOrder(order);
            menuBar.setParent(parent);
            menuBar.setEnable(enable);
        }

        boolean b = false;
        if(menuBarId == null){
            b = menuBarService.save(menuBar);
        }else{
            b = menuBarService.update(menuBar,new UpdateWrapper<MenuBarEntity>()
                    .lambda()
                    .set(MenuBarEntity::getParent,parent)
                    .eq(MenuBarEntity::getMenuBarId,menuBarId));
        }

        return b ? ResponseResult.success() : ResponseResult.error("保存失败");
    }

    @ApiOperation(value = "菜单列表" ,httpMethod = "GET")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResponseResult menuBarPage(
            @NotBlank(message = "当前页码不能为空")
            @ApiParam(name="pageNo",value = "当前页码",required = true)
            @RequestParam(required = true) int pageNo,

            @NotBlank(message = "每页行数不能为空")
            @ApiParam(name="pageSize",value = "每页行数",required = true)
            @RequestParam(required = true) int pageSize,

            @ApiParam(name="isMainMenu",value = "是否一级菜单(null查询全部)",required = false)
            @RequestParam(required = false) Boolean isMainMenu){

        LambdaQueryWrapper<MenuBarEntity> select = new QueryWrapper<MenuBarEntity>().lambda().select(MenuBarEntity::getMenuBarId, MenuBarEntity::getName, MenuBarEntity::getEnable);
        if(isMainMenu != null){
            if(isMainMenu){
                select.isNull(MenuBarEntity::getParent);
            }else{
                select. isNotNull(MenuBarEntity::getParent);
            }
        }
        Page<MenuBarEntity> page = menuBarService.page(pageNo, pageSize, select);
        return ResponseResult.success(page);
    }

    @ApiOperation(value = "根据菜单id获取菜单详情" ,httpMethod = "GET")
    @RequestMapping(value = "/get/{menuBarId}",method = RequestMethod.GET)
    public ResponseResult getById(
            @NotBlank(message = "菜单id不能为空")
            @ApiParam(name="menuBarId",value = "菜单id",required = true)
            @PathVariable(required = true) Long menuBarId){
        MenuBarEntity menuBar = menuBarService.getById(menuBarId);
        return menuBar == null ? ResponseResult.error("该菜单不存在") : ResponseResult.success(menuBar);
    }

    @ApiOperation(value = "启用/禁用菜单" ,httpMethod = "PUT")
    @RequestMapping(value = "/status/{menuBarId}/{type}",method = RequestMethod.PUT)
    public ResponseResult changeMenuBarStatus(
            @NotBlank(message = "菜单id不能为空")
            @ApiParam(name="menuBarId",value = "菜单id",required = true)
            @PathVariable(required = true) Long menuBarId,

            @NotBlank(message = "请选择 启用/禁用")
            @Pattern(regexp = "^(enable|disable)$",message = "状态值不正确")
            @ApiParam(name="type",value = "启用/禁用",allowableValues = "enable,disable",required = true)
            @PathVariable(required = true) String type){
        if(StringPool.ENABLE.equals(type)){
            menuBarService.enableOrDisable(menuBarId,true);
        }else{
            menuBarService.enableOrDisable(menuBarId,false);
        }
        return ResponseResult.success();
    }

    @ApiOperation(value = "删除菜单" ,httpMethod = "DELETE")
    @RequestMapping(value = "/delete/{menuBarId}",method = RequestMethod.DELETE)
    public ResponseResult remove(
            @NotBlank(message = "菜单id不能为空")
            @ApiParam(name="menuBarId",value = "菜单id",required = true)
            @PathVariable(required = true) Long menuBarId){
        boolean b = menuBarService.removeById(menuBarId);
        return b ? ResponseResult.success() : ResponseResult.error("删除失败");
    }
}

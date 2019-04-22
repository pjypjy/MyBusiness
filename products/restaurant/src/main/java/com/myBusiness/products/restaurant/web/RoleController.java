package com.myBusiness.products.restaurant.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.common.util.IdUtil;
import com.myBusiness.common.util.StringPool;
import com.myBusiness.products.restaurant.entity.RoleEntity;
import com.myBusiness.products.restaurant.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Api(description = "角色管理")
@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @ApiOperation(value = "新增/修改 角色" ,httpMethod = "POST")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseResult saveRole(
            @ApiParam(name="menuBarId",value = "角色id,不为null表示修改,否则表示新增",required = false)
            @RequestParam(required = false) Long roleId,

            @NotBlank(message = "角色名称不能为空")
            @Size(min = 2,max = 8,message = "角色名称长度为2~8位字符")
            @ApiParam(name="name",value = "角色名称",required = true)
            @RequestParam(required = true) String name,

            @NotBlank(message = "角色描述不能为空")
            @Size(max = 128,message = "角色描述最大长度为128位字符")
            @ApiParam(name="description",value = "角色描述",required = true)
            @RequestParam(required = true) String description,

            @NotBlank(message = "角色顺序号不能为空")
            @ApiParam(name="order",value = "角色顺序号",required = true)
            @RequestParam(required = true) int order,

            @NotBlank(message = "请选择是否启用")
            @ApiParam(name="enable",value = "是否启用",required = true)
            @RequestParam(required = true) Boolean enable){

        RoleEntity role = new RoleEntity();
        if(roleId == null){
            //新增
            role.setRoleId(IdUtil.genRoleId());
            role.setName(name);
            role.setDescription(description);
            role.setOrder(order);
            role.setEnable(enable);
        }else{
            //修改
            role = roleService.getById(roleId);
            if(role == null){
                return ResponseResult.error("该角色不存在");
            }
            role.setName(name);
            role.setDescription(description);
            role.setOrder(order);
            role.setEnable(enable);
        }
        boolean b = roleService.saveOrUpdate(role);
        if(b){
            return ResponseResult.success(role.getRoleId());
        }else{
            return ResponseResult.error("保存失败");
        }
    }

    @ApiOperation(value = "角色列表" ,httpMethod = "GET")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResponseResult rolePage(
            @NotBlank(message = "当前页码不能为空")
            @ApiParam(name="pageNo",value = "当前页码",required = true)
            @RequestParam(required = true) int pageNo,

            @NotBlank(message = "每页行数不能为空")
            @ApiParam(name="pageSize",value = "每页行数",required = true)
            @RequestParam(required = true) int pageSize){

        LambdaQueryWrapper<RoleEntity> select = new QueryWrapper<RoleEntity>().lambda().select(RoleEntity::getRoleId, RoleEntity::getName, RoleEntity::getEnable);
        Page<RoleEntity> page = roleService.page(pageNo, pageSize, select);
        return ResponseResult.success(page);
    }

    @ApiOperation(value = "根据角色id获取角色详情" ,httpMethod = "GET")
    @RequestMapping(value = "/get/{roleId}",method = RequestMethod.GET)
    public ResponseResult getById(
            @NotBlank(message = "角色id不能为空")
            @ApiParam(name="roleId",value = "角色id",required = true)
            @PathVariable(required = true) Long roleId){
        RoleEntity role = roleService.getById(roleId);
        return role == null ? ResponseResult.error("该角色不存在") : ResponseResult.success(role);
    }

    @ApiOperation(value = "启用/禁用角色" ,httpMethod = "PUT")
    @RequestMapping(value = "/status/{roleId}/{type}",method = RequestMethod.PUT)
    public ResponseResult changeMenuBarStatus(
            @NotBlank(message = "角色id不能为空")
            @ApiParam(name="roleId",value = "角色id",required = true)
            @PathVariable(required = true) Long roleId,

            @NotBlank(message = "请选择 启用/禁用")
            @Pattern(regexp = "^(enable|disable)$",message = "状态值不正确")
            @ApiParam(name="type",value = "启用/禁用",allowableValues = "enable,disable",required = true)
            @PathVariable(required = true) String type){
        if(StringPool.ENABLE.equals(type)){
            roleService.enableOrDisable(roleId,true);
        }else{
            roleService.enableOrDisable(roleId,false);
        }
        return ResponseResult.success();
    }

    @ApiOperation(value = "删除角色" ,httpMethod = "DELETE")
    @RequestMapping(value = "/delete/{roleId}",method = RequestMethod.DELETE)
    public ResponseResult remove(
            @NotBlank(message = "角色id不能为空")
            @ApiParam(name="roleId",value = "角色id",required = true)
            @PathVariable(required = true) Long roleId){
        boolean b = roleService.removeById(roleId);
        return b ? ResponseResult.success() : ResponseResult.error("删除失败");
    }

}

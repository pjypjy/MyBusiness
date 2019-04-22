package com.myBusiness.products.restaurant.web;

import com.myBusiness.common.exception.RoleNotFoundException;
import com.myBusiness.common.exception.UserNotFoundException;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.entity.MenuBarEntity;
import com.myBusiness.products.restaurant.service.AuthorityService;
import com.myBusiness.products.restaurant.service.MenuBarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(description = "权限管理")
@RestController
@RequestMapping(value = "/authority")
@Validated
public class AuthorityController {

    @Autowired
    AuthorityService authorityService;

    @Autowired
    MenuBarService menuBarService;

    @ApiOperation(value = "修改用户的角色" ,httpMethod = "POST")
    @RequestMapping(value = "/role/{roleId}/user/{userId}",method = RequestMethod.POST)
    public ResponseResult saveRoleForUser(
            @NotBlank(message = "角色id不能为空")
            @ApiParam(name="roleId",value = "角色id",required = true)
            @PathVariable(required = true) Long roleId,

            @NotBlank(message = "用户id不能为空")
            @ApiParam(name="userId",value = "角色id",required = true)
            @PathVariable(required = true) Long userId){

        try {
            boolean flag = authorityService.saveRoleForUser(roleId, userId);
            return flag ? ResponseResult.success() : ResponseResult.error("保存失败");
        } catch (RoleNotFoundException e) {
            return ResponseResult.error("该角色不存在");
        } catch (UserNotFoundException e) {
            return ResponseResult.error("该用户不存在");
        }
    }

    @ApiOperation(value = "为角色增加可用菜单" ,httpMethod = "POST")
    @RequestMapping(value = "/role/{roleId}/user/{menuBarId}",method = RequestMethod.POST)
    public ResponseResult addMenuForRole(
            @NotBlank(message = "角色id不能为空")
            @ApiParam(name="roleId",value = "角色id",required = true)
            @PathVariable(required = true) Long roleId,

            @NotBlank(message = "菜单id不能为空")
            @ApiParam(name="menuBarId",value = "菜单id",required = true)
            @PathVariable(required = true) Long menuBarId){

        return authorityService.saveMenuBarForRole(menuBarId,roleId);
    }


    @ApiOperation(value = "为角色删除可用菜单" ,httpMethod = "DELETE")
    @RequestMapping(value = "/role/{roleId}/user/{menuBarId}",method = RequestMethod.DELETE)
    public ResponseResult deleteMenuForRole(
            @NotBlank(message = "角色id不能为空")
            @ApiParam(name="roleId",value = "角色id",required = true)
            @PathVariable(required = true) Long roleId,

            @NotBlank(message = "菜单id不能为空")
            @ApiParam(name="menuBarId",value = "菜单id",required = true)
            @PathVariable(required = true) Long menuBarId){

        return authorityService.deleteMenuBarForRole(menuBarId,roleId);
    }

    @ApiOperation(value = "查询某角色的可用菜单" ,httpMethod = "GET")
    @RequestMapping(value = "/role/{roleId}/menus",method = RequestMethod.GET)
    public ResponseResult findMenuByRoleId(
            @NotBlank(message = "角色id不能为空")
            @ApiParam(name="roleId",value = "角色id",required = true)
            @PathVariable(required = true) Long roleId){

        List<MenuBarEntity> menuBarByRoleId = menuBarService.findMenuBarByRoleId(roleId);
        return ResponseResult.success(menuBarByRoleId);
    }
}

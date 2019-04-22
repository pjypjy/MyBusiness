package com.myBusiness.products.restaurant.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myBusiness.common.exception.UserNotFoundException;
import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.config.token.TokenManager;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.common.util.IdUtil;
import com.myBusiness.common.util.StringPool;
import com.myBusiness.products.restaurant.config.parambind.LocalDateValid;
import com.myBusiness.products.restaurant.entity.AdminUserEntity;
import com.myBusiness.products.restaurant.service.AdminUserService;
import com.myBusiness.products.restaurant.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Api(description = "用户管理")
@RestController
@RequestMapping(value = "/api/user")
@Validated
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    AdminUserService adminUserService;

    @Autowired
    TokenManager tokenManager;

    @ApiOperation(value = "根据id查询 后台管理用户" ,httpMethod = "GET")
    @RequestMapping(value = "/adminUser/get/{userId}",method = RequestMethod.GET)
    public ResponseResult getUserById(
            @NotBlank(message = "用户id不能为空")
            @ApiParam(name="userId",value = "用户id",required = true)
            @PathVariable(required = true) Long userId){
        AdminUserEntity adminUserEntity = adminUserService.getById(userId);
        return adminUserEntity == null ? ResponseResult.error("该用户不存在") : ResponseResult.success(adminUserEntity);
    }

    @ApiOperation(value = "后台用户列表" ,httpMethod = "GET")
    @RequestMapping(value = "/adminUser/list",method = RequestMethod.GET)
    public ResponseResult page(
            @NotBlank(message = "当前页码不能为空")
            @ApiParam(name="pageNo",value = "当前页码",required = true)
            @RequestParam(required = true) int pageNo,

            @NotBlank(message = "每页行数不能为空")
            @ApiParam(name="pageSize",value = "每页行数",required = true)
            @RequestParam(required = true) int pageSize){

        QueryWrapper<AdminUserEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AdminUserEntity> query = queryWrapper.lambda().select(AdminUserEntity::getUserId, AdminUserEntity::getSex, AdminUserEntity::getName,AdminUserEntity::getEnable);
        Page<AdminUserEntity> page = adminUserService.page(pageNo, pageSize, query);
        return ResponseResult.success(page);
    }

    @ApiOperation(value = "增加/修改 后台管理用户" ,httpMethod = "POST")
    @RequestMapping(value = "/adminUser/save",method = RequestMethod.POST)
    public ResponseResult saveAdminUserInfo(
            @ApiParam(name="userId",value = "后台用户id(此处为空表示新增，否则表示修改)",required = false)
            @RequestParam(required = false,name = "userId") Long userId,

            @NotBlank(message = "登录账号不能为空")
            @Size(min = 3,max=16,message = "登录账号长度必须为3~16个字符")
            @ApiParam(name="loginId",value = "登录账号",required = true)
            @RequestParam(required = true) String loginId,

            @NotBlank(message = "姓名不能为空")
            @Size(min = 2,max=16,message = "姓名必须为2~16位字符")
            @ApiParam(name="name",value = "姓名",required = true)
            @RequestParam(required = true) String name,

            @NotBlank(message = "手机号码不能为空")
            @Pattern(regexp = "^1([0-9]{10})$",message = "手机号码格式不正确")
            @ApiParam(name="phone",value = "手机号",required = true)
            @RequestParam(required = true) String phone,

            @NotBlank(message = "密码不能为空")
            @Size(min = 6,max = 24,message = "密码长度必须为6~24个字符")
            @ApiParam(name="password",value = "密码",required = true)
            @RequestParam(required = true) String password,

            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @LocalDateValid(canEmpty = false,message = "生日不能为空")
            @ApiParam(name="birthday",value = "生日",required = true)
            @RequestParam(required = true) LocalDate birthday,

            @NotBlank(message = "性别不能为空")
            @Pattern(regexp = "^(0001-1|0001-2|0001-3)$",message = "性别不正确")
            @ApiParam(name="sex",value = "性别",required = true,allowableValues = "0001-1,0001-2,0001-3")
            @RequestParam(required = true) String sex){

        AdminUserEntity adminUserEntity = null;

        if(userId == null){
            adminUserEntity = new AdminUserEntity();
            adminUserEntity.setUserId(IdUtil.genUserId());
            adminUserEntity.setPasswordSuffix(String.valueOf(CommonUtil.randomPasswordSuffix()));
        }else{
            adminUserEntity = adminUserService.getById(userId);
            if(adminUserEntity == null){
                return ResponseResult.error("用户不存在");
            }
        }

        adminUserEntity.setLoginId(loginId);
        adminUserEntity.setName(name);
        adminUserEntity.setPhone(phone);
        adminUserEntity.setLocalPassword(password);
        adminUserEntity.setBirthday(birthday);
        adminUserEntity.setSex(sex);

        String encodedPassword = adminUserEntity.getLocalPassword().concat(adminUserEntity.getPasswordSuffix());
        adminUserEntity.setLocalPassword(CommonUtil.encodeMD5(encodedPassword));

        adminUserService.saveOrUpdate(adminUserEntity);
        return ResponseResult.success();
    }

    @ApiOperation(value = "启用/禁用 后台管理用户" ,httpMethod = "PUT")
    @RequestMapping(value = "/adminUser/status/{userId}/{type}",method = RequestMethod.PUT)
    public ResponseResult changeUserStatus(
            @NotBlank(message = "用户id不能为空")
            @ApiParam(name="userId",value = "用户id",required = true)
            @PathVariable(required = true) Long userId,

            @NotBlank(message = "请选择 启用/禁用")
            @Pattern(regexp = "^(enable|disable)$",message = "状态值不正确")
            @ApiParam(name="type",value = "启用/禁用",allowableValues = "enable,disable",required = true)
            @PathVariable(required = true) String type){

        String token = null;
        try {
            token = adminUserService.getToken(userId);
        } catch (UserNotFoundException e) {
            return ResponseResult.error("用户不存在");
        }
        if(CommonUtil.isNotEmpty(token)){
            if(StringPool.ENABLE.equals(type)){
                adminUserService.enableOrDisableUser(userId,true);
                //禁用成功，直接让redis的token剔除
                tokenManager.delete(token);
            }else{
                adminUserService.enableOrDisableUser(userId,false);
            }
        }
        return ResponseResult.success();
    }

    @ApiOperation(value = "删除 后台管理用户" ,httpMethod = "DELETE")
    @RequestMapping(value = "/adminUser/delete/{userId}",method = RequestMethod.DELETE)
    public ResponseResult remove(
            @NotBlank(message = "用户id不能为空")
            @ApiParam(name="userId",value = "用户id",required = true)
            @PathVariable(required = true) Long userId){
        boolean b = adminUserService.removeById(userId);
        if(b){
            return ResponseResult.success();
        }else{
            return ResponseResult.error("删除失败");
        }
    }

    @ApiOperation(value = "用户列表" ,httpMethod = "DELETE")
    @RequestMapping(value = "/customer/list",method = RequestMethod.DELETE)
    public ResponseResult list(
            @NotBlank(message = "当前页码不能为空")
            @ApiParam(name="pageNo",value = "当前页码",required = true)
            @RequestParam(required = true) int pageNo,

            @NotBlank(message = "每页行数不能为空")
            @ApiParam(name="pageSize",value = "每页行数",required = true)
            @RequestParam(required = true) int pageSize,

            @Pattern(regexp = "^1([0-9]{10})$",message = "手机号码格式不正确")
            @ApiParam(name="phone",value = "用户手机号",required = true)
            @RequestParam(required = true) String phone){

        return ResponseResult.success(userInfoService.page(pageNo, pageSize, phone));
    }
}

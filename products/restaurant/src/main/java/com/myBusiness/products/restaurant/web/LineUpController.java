package com.myBusiness.products.restaurant.web;

import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.products.restaurant.service.LineUpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "排队")
@RestController
@RequestMapping(value = "/lineUp")
@Validated
public class LineUpController {

    @Autowired
    LineUpService lineUpService;

    @ApiOperation(value = "增加一个人排队" ,httpMethod = "POST")
    @RequestMapping(value = "/in/{userId}",method = RequestMethod.POST)
    public ResponseResult inLineUp(
            @ApiParam(name="userId",value = "用户id",required = true)
            @PathVariable(required = true) Long userId){
        Long b = lineUpService.inLineUp(userId);
        return b == null ? ResponseResult.error("排队失败") : ResponseResult.success(b);
    }

    @ApiOperation(value = "从排队列表去除序号最前面的一个人" ,httpMethod = "POST")
    @RequestMapping(value = "/out",method = RequestMethod.POST)
    public ResponseResult outLineUp(){
        Long userId = lineUpService.outLineUp();
        return userId == null ? ResponseResult.error("当前无人排队") : ResponseResult.success(userId);
    }

    @ApiOperation(value = "重置序号，应用:当天营业结束，重置序号，第二天重新开始计序号" ,httpMethod = "POST")
    @RequestMapping(value = "/reset",method = RequestMethod.POST)
    public ResponseResult resetLineUp(){
        lineUpService.resetLineUp();
        return ResponseResult.success();
    }

    @ApiOperation(value = "查询下一个序号" ,httpMethod = "GET")
    @RequestMapping(value = "/next",method = RequestMethod.GET)
    public ResponseResult nextInLineUp(){
        Long next = lineUpService.next();
        return next == null ? ResponseResult.error("当前无人排队") : ResponseResult.success(next);
    }
}

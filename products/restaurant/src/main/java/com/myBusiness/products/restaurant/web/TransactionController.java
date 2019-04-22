package com.myBusiness.products.restaurant.web;

import com.myBusiness.products.restaurant.entity.AdminUserEntity;
import com.myBusiness.products.restaurant.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test/transaction")
public class TransactionController {

    @Autowired
    AdminUserService adminUserService;

    @RequestMapping(value = "/getData/unCommit/{userId}")
    public AdminUserEntity getDataUnCommit(@PathVariable(name = "userId") Long userId){
        AdminUserEntity data = adminUserService.getDataUnCommit(userId);
        return data;
    }

    @RequestMapping(value = "/getData/committed")
    public AdminUserEntity getDataCommit(){
        AdminUserEntity data = adminUserService.getDataCommitted();
        return data;
    }

    @RequestMapping(value = "/getData/repeatable")
    public AdminUserEntity getDataRepeatable(){
        AdminUserEntity data = adminUserService.getDataRepeatable();
        return data;
    }

    @RequestMapping(value = "/getData/serializable")
    public AdminUserEntity getDataSerializable(){
        AdminUserEntity data = adminUserService.getDataSerializable();
        return data;
    }

    @RequestMapping(value = "/update/{userId}/{name}")
    public int update(@PathVariable(name = "userId") Long userId,@PathVariable(name = "name") String name){
        int i = adminUserService.update(userId,name);
        return i;
    }

    @RequestMapping(value = "/insert/{name}")
    public long insert(@PathVariable(name = "name") String name){
        long i = adminUserService.insert(name);
        return i;
    }

    @RequestMapping(value = "/insert/{userId}/{name}")
    public long insert(@PathVariable(name = "userId") Long userId,@PathVariable(name = "name") String name){
        long i = adminUserService.insert(userId,name);
        return i;
    }

    @RequestMapping(value = "/selectAndUpdate/{name}")
    public int selectAndUpdate(@PathVariable(name = "name") String name){
        int i = adminUserService.selectAndUpdate(name);
        return i;
    }

    @RequestMapping(value = "/selectAndUpdate2/{name}")
    public int selectAndUpdate2(@PathVariable(name = "name") String name){
        int i = adminUserService.selectAndUpdate2(name);
        return i;
    }

    @RequestMapping(value = "/selectAndUpdate3/{name}")
    public int selectAndUpdate3(@PathVariable(name = "name") String name){
        int i = adminUserService.selectAndUpdate3(name);
        return i;
    }
}

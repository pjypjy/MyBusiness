package com.myBusiness.products.restaurant.web;


import com.myBusiness.common.model.ResponseResult;
import com.myBusiness.common.util.CommonUtil;
import com.myBusiness.products.restaurant.config.FilePathConfig;
import com.myBusiness.products.restaurant.entity.GoodsEntity;
import com.myBusiness.products.restaurant.service.GoodsCategoryService;
import com.myBusiness.products.restaurant.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.*;
import java.math.BigDecimal;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pjy
 * @since 2018-09-25
 */
@Api(description = "商品管理")
@RestController
@RequestMapping(value = "/goods")
@Validated
public class GoodsController {

    private final static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    FilePathConfig filePathConfig;

    @Autowired
    GoodsService goodsService;

    @Autowired
    GoodsCategoryService goodsCategoryService;

    @ApiOperation(value = "增加/修改 商品" ,httpMethod = "POST")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResponseResult save(
            @ApiParam(name="goodsId",value = "商品id",required = false)
            @RequestParam(required = false) Integer goodsId,

            @NotBlank(message = "商品名称不能为空")
            @Size(min = 1,max=16,message = "商品名称长度为1~32个字符")
            @ApiParam(name="name",value = "商品名称",required = true)
            @RequestParam(required = true) String name,

            @NotBlank(message = "商品描述不能为空")
            @Size(max=128,message = "商品描述长度最大长度为128个字符")
            @ApiParam(name="description",value = "商品描述",required = true)
            @RequestParam(required = true) String description,

            @NotBlank(message = "请选择是否启用库存管理")
            @ApiParam(name="enableAmount",value = "是否启用库存管理",required = true)
            @RequestParam(required = true) Boolean enableAmount,

            @ApiParam(name="amount",value = "库存数量",required = false,defaultValue = "0")
            @RequestParam(required = false,defaultValue = "0") Integer amount,

            @ApiParam(name="file",value = "商品图片",required = false)
            @RequestParam(value="file",required = false) MultipartFile file,

            @ApiParam(name="price",value = "商品价格",required = false)
            @RequestParam(required = false) BigDecimal price,

            @NotBlank(message = "商品类别不能为空")
            @ApiParam(name="categoryId",value = "商品类别id",required = true)
            @RequestParam(required = true) Integer categoryId){

        //如果大于5Mb返回错误 1MB = 1*1024*1024 byte（字节B）
        String originalFilename = file.getOriginalFilename();
        if(!file.isEmpty()){
            if(file.getSize() > 5 * 1024 * 1024L){
                return ResponseResult.error("文件过大(最大为5MB)");
            }
            if(!CommonUtil.isImg(originalFilename)){
                return ResponseResult.error("仅支持jpg、jpeg、png、bmg、gif文件");
            }
        }

        if(price == null){
            return ResponseResult.error("商品价格不能为空");
        }
        if(price.compareTo(new BigDecimal(99999999)) > 0){
            return ResponseResult.error("商品价格最高为99999999");
        }

        //类别判断
        Boolean existsCategory = goodsCategoryService.exists(categoryId);
        if(!Boolean.TRUE.equals(existsCategory)){
            return ResponseResult.error("该商品类别不存在");
        }

        GoodsEntity goodsEntity = null;
        if(goodsId == null){
            goodsEntity = new GoodsEntity();
        }else{
            goodsEntity = goodsService.getById(goodsId);
            if(goodsEntity == null){
                return ResponseResult.error("该商品不存在");
            }
        }

        goodsEntity.setName(name);
        goodsEntity.setDescription(description);
        goodsEntity.setPrice(price);
        goodsEntity.setCategoryId(categoryId);
        goodsEntity.setEnableAmount(enableAmount);
        goodsEntity.setAmount(amount);

        //设置默认的图片
        if(file.isEmpty()){
            goodsEntity.setImg(CommonUtil.genGoodsImgUrl("default.jpg"));
            goodsService.saveOrUpdate(goodsEntity);
            return ResponseResult.success();
        }

        //保存/新增后 数据库中的主键
        goodsId = goodsEntity.getGoodsId();

        //以goodsId为文件名重命名文件
        String newFileName = CommonUtil.renameFile(originalFilename, String.valueOf(goodsId));

        //新的完整本地路径的文件名
        String goodImgLocalPath = filePathConfig.getGoodsImgBaseLocalPath() + File.separator + newFileName;

        //写入本地
        try (OutputStream os = new FileOutputStream(goodImgLocalPath)){
            os.write(file.getBytes());
        } catch (FileNotFoundException e) {
            logger.error("文件夹路径不存在",e);
            return ResponseResult.error("文件夹路径不存在");
        } catch (IOException e) {
            logger.error("写入文件失败",e);
            return ResponseResult.error("写入文件失败");
        }

        //设置新的图片
        goodsEntity.setImg(CommonUtil.genGoodsImgUrl(newFileName));
        goodsService.saveOrUpdate(goodsEntity);

        //删除原先的图片文件 除了 这一次上传的这一张
        File goodsImgBaseDir = new File(filePathConfig.getGoodsImgBaseLocalPath());
        File[] files = goodsImgBaseDir.listFiles();
        if(files != null){
            for(File oldFile : files){
                logger.info("file:{}",oldFile.getName());
                if(oldFile.getName().startsWith(String.valueOf(goodsId).concat(".")) && !oldFile.getName().equals(newFileName)){
                    logger.info("delete file:{}",oldFile.getName());
                    oldFile.delete();
                }
            }
        }
        return ResponseResult.success();
    }


    @ApiOperation(value = "商品列表" ,httpMethod = "GET")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResponseResult goodsList(
            @NotBlank(message = "当前页码不能为空")
            @ApiParam(name="pageNo",value = "当前页码",required = true)
            @RequestParam(required = true) int pageNo,

            @NotBlank(message = "每页行数不能为空")
            @ApiParam(name="pageSize",value = "每页行数",required = true)
            @RequestParam(required = true) int pageSize,

            @ApiParam(name="categoryId",value = "商品类别id",required = false)
            @RequestParam(required = false) Integer categoryId,

            @Size(min = 1,max=16,message = "商品名称长度为1~32个字符")
            @ApiParam(name="name",value = "商品名称",required = false)
            @RequestParam(required = false) String name){

        return ResponseResult.success(goodsService.pageQuery(pageNo,pageSize,categoryId,name));
    }

    @ApiOperation(value = "商品详情" ,httpMethod = "GET")
    @RequestMapping(value = "/get/{goodsId}",method = RequestMethod.GET)
    public ResponseResult get(
            @NotBlank(message = "商品id不能为空")
            @ApiParam(name="goodsId",value = "商品id",required = true)
            @PathVariable(required = true) Integer goodsId){

        GoodsEntity goodsEntity = goodsService.getById(goodsId);
        if(goodsEntity == null){
            return ResponseResult.error("该商品不存在");
        }
        return ResponseResult.success(goodsEntity);
    }

    @ApiOperation(value = "禁用/启用 商品" ,httpMethod = "PUT")
    @RequestMapping(value = "/status/{goodsId}/{type}",method = RequestMethod.PUT)
    public ResponseResult status(
            @NotBlank(message = "商品id不能为空")
            @ApiParam(name="goodsId",value = "商品id",required = true)
            @PathVariable(required = true) Integer goodsId,

            @NotBlank(message = "请选择 启用/禁用")
            @Pattern(regexp = "^(enable|disable)$",message = "状态值不正确")
            @ApiParam(name="type",value = "启用/禁用",allowableValues = "enable,disable",required = true)
            @PathVariable(required = true) String type){

        return goodsService.dealGoodsStatus(goodsId,type);
    }

    @ApiOperation(value = "禁用/启用库存管理" ,httpMethod = "PUT")
    @RequestMapping(value = "/amountStatus/{goodsId}/{type}",method = RequestMethod.PUT)
    public ResponseResult changeAmountStatus(
            @NotBlank(message = "商品id不能为空")
            @ApiParam(name="goodsId",value = "商品id",required = true)
            @PathVariable(required = true) Integer goodsId,

            @NotBlank(message = "请选择 启用/禁用")
            @Pattern(regexp = "^(enable|disable)$",message = "状态值不正确")
            @ApiParam(name="type",value = "启用/禁用",allowableValues = "enable,disable",required = true)
            @PathVariable(required = true) String type){

        return goodsService.dealAmountStatus(goodsId,type);
    }

    @ApiOperation(value = "增加/减少 库存数量" ,httpMethod = "PUT")
    @RequestMapping(value = "/amount/{goodsId}/{changeAmount}",method = RequestMethod.PUT)
    public ResponseResult changeAmount(
            @NotBlank(message = "商品id不能为空")
            @ApiParam(name="goodsId",value = "商品id",required = true)
            @PathVariable(required = true) Integer goodsId,

            @NotBlank(message = "改变的库存数量不能为空")
            @ApiParam(name="changeAmount",value = "改变的库存数量(负数表示减少,非负表示增加)",required = true)
            @PathVariable(required = true) Integer changeAmount){

        return goodsService.changeAmount(goodsId,changeAmount);
    }

    @ApiOperation(value = "开启/关闭 折扣" ,httpMethod = "PUT")
    @RequestMapping(value = "/discountStatus/{goodsId}/{type}",method = RequestMethod.PUT)
    public ResponseResult changeDiscountStatus(
            @NotBlank(message = "商品id不能为空")
            @ApiParam(name="goodsId",value = "商品id",required = true)
            @PathVariable(required = true) Integer goodsId,

            @NotBlank(message = "请选择 启用/禁用")
            @Pattern(regexp = "^(enable|disable)$",message = "状态值不正确")
            @ApiParam(name="type",value = "启用/禁用",allowableValues = "enable,disable",required = true)
            @PathVariable(required = true) String type,

            @ApiParam(name="discount",value = "折扣值(例如:9.5代表9.5折)",required = false)
            @RequestParam(required = false) BigDecimal discount){

        return goodsService.dealDiscountStatus(goodsId, type, discount);
    }


    @ApiOperation(value = "删除商品" ,httpMethod = "DELETE")
    @RequestMapping(value = "/delete/{goodsId}",method = RequestMethod.DELETE)
    public ResponseResult delete(
            @NotBlank(message = "商品id不能为空")
            @ApiParam(name="goodsId",value = "商品id",required = true)
            @PathVariable(required = true) Integer goodsId){

        boolean b = goodsService.removeById(goodsId);
        return b ? ResponseResult.success() : ResponseResult.error("删除失败");
    }
}

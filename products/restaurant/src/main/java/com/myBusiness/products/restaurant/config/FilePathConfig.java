package com.myBusiness.products.restaurant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.File;

@Configuration
@ConfigurationProperties(prefix="file")
@PropertySource(value="classpath:application.properties")
public class FilePathConfig {

    private final static Logger logger = LoggerFactory.getLogger(FilePathConfig.class);

    private String goodsImgBaseLocalPath;

    public String getGoodsImgBaseLocalPath() {
        return goodsImgBaseLocalPath;
    }

    public void setGoodsImgBaseLocalPath(String goodsImgBaseLocalPath) {
        File dir = new File(goodsImgBaseLocalPath);
        logger.info("商品图片本地目录：{}",goodsImgBaseLocalPath);
        if(!dir.exists()){
            logger.info("创建：{}",goodsImgBaseLocalPath);
            dir.mkdirs();
        }
        this.goodsImgBaseLocalPath = goodsImgBaseLocalPath;
    }

}

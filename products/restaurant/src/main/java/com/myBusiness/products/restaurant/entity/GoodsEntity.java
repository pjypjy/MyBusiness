package com.myBusiness.products.restaurant.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import com.myBusiness.common.util.CommonUtil;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-09-25
 */
@TableName("goods")
public class GoodsEntity implements Serializable {

    private static final long serialVersionUID = -5524298232639117628L;

    @TableId(value = "goods_id",type = IdType.AUTO)
    private Integer goodsId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("img")
    private String img;

    @TableField("price")
    private BigDecimal price;

    @TableId(value = "category_id")
    private Integer categoryId;

    @TableField("enableAmount")
    private Boolean enableAmount;

    @TableField("amount")
    private Integer amount;

    @TableField("enableDiscount")
    private Boolean enableDiscount;

    @TableField("discount")
    private BigDecimal discount;

    @TableField("enable")
    private Boolean enable;

    @TableField("version")
    private Long version;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = CommonUtil.setRoundHalfUpKeep2DecimalPlaces(price);
    }
    public Boolean getEnableAmount() {
        return enableAmount;
    }

    public void setEnableAmount(Boolean enableAmount) {
        this.enableAmount = enableAmount;
    }
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Boolean getEnableDiscount() {
        return enableDiscount;
    }

    public void setEnableDiscount(Boolean enableDiscount) {
        this.enableDiscount = enableDiscount;
    }
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GoodsEntity{" +
                "goodsId=" + goodsId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", img='" + img + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                ", enableAmount=" + enableAmount +
                ", amount=" + amount +
                ", enableDiscount=" + enableDiscount +
                ", discount=" + discount +
                ", enable=" + enable +
                ", version=" + version +
                '}';
    }
}

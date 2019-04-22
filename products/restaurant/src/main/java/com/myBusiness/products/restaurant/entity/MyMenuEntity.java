package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-09-27
 */
@TableName("my_menu")
public class MyMenuEntity implements Serializable {

    private static final long serialVersionUID = 1070852475251931600L;

    @TableField("user_id")
    private Long userId;

    @TableField("`index`")
    private Integer index;

    @TableField("menu_name")
    private String menuName;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("last_update_date")
    private LocalDateTime lastUpdateDate;

    @TableField("goods_info")
    private String goodsInfo;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public String toString() {
        return "MyMenuEntity{" +
        "userId=" + userId +
        ", index=" + index +
        ", menuName=" + menuName +
        ", createDate=" + createDate +
        ", lastUpdateDate=" + lastUpdateDate +
        ", goodsInfo=" + goodsInfo +
        "}";
    }
}

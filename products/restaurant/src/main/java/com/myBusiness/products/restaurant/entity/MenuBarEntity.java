package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-09-20
 */
@TableName("menu_bar")
public class MenuBarEntity implements Serializable {

    private static final long serialVersionUID = 7152630706972682650L;

    @TableId("menu_bar_id")
    private Long menuBarId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("url")
    private String url;

    @TableField("order")
    private Integer order;

    @TableField("parent")
    private Long parent;

    @TableField("enable")
    private Boolean enable;

    @TableField(exist=false)
    private List<MenuBarEntity> sonMenuBars;

    public Long getMenuBarId() {
        return menuBarId;
    }

    public void setMenuBarId(Long menuBarId) {
        this.menuBarId = menuBarId;
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
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public List<MenuBarEntity> getSonMenuBars() {
        return sonMenuBars;
    }

    public void setSonMenuBars(List<MenuBarEntity> sonMenuBars) {
        this.sonMenuBars = sonMenuBars;
    }

    @Override
    public String toString() {
        return "MenuBarEntity{" +
                "menuBarId=" + menuBarId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", order=" + order +
                ", parent=" + parent +
                ", enable=" + enable +
                ", sonMenuBars=" + sonMenuBars +
                '}';
    }
}

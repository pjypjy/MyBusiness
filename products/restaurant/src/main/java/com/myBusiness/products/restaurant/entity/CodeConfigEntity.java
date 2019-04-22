package com.myBusiness.products.restaurant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author pjy
 * @since 2018-11-15
 */
@TableName("code_config")
public class CodeConfigEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("code")
    private String code;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "CodeConfigEntity{" +
        "code=" + code +
        ", name=" + name +
        ", description=" + description +
        "}";
    }
}

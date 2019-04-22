package com.myBusiness.products.restaurant.enums;

public enum OrderStatus {

    COMMITTED("0002-1","已提交"),
    PAID("0002-2","已付款"),
    COMPLETE("0002-2","已完成"),
    CANCELLING("0002-3","取消中"),
    CANCELLED("0002-4","已取消"),
    TIMEOUT_CLOSED("0002-5","已超时关闭");

    private String code;

    private String name;

    OrderStatus(String code,String name){
        this.code = code;
        this.name = name;
    }

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
}

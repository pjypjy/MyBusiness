package com.myBusiness.common.model;

public enum ResponseCodeEnum {

    SUCCESS(0,"成功"),
    ERROR(1,"失败"),
    TOKEN_EXPIRE(2,"登录信息过期，请重新登录"),
    NO_PERMISSION(3,"没有权限"),
    REQUEST_ILLEGAL(4,"非法请求"),
    PARAMETER_LACK(5,"缺少参数"),
    PARAMETER_ILLEGAL(5,"参数值不允许");

    private int code;

    private String msg;

    private ResponseCodeEnum(int code,String msg){
       this.code = code;
       this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

package com.myBusiness.common.model;

import java.io.Serializable;

/**
 * 返回数据包装类
 */
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = 4298841955409622346L;

    private int code;

    private String msg;

    private Object data;

    private ResponseResult(Integer code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResponseResult success(){
        return success(null);
    }

    public static ResponseResult success(Object data){
        return new ResponseResult(ResponseCodeEnum.SUCCESS.getCode(),ResponseCodeEnum.SUCCESS.getMsg(),data);
    }

    /**
     * 失败 返回自定义错误信息
     * @param msg
     * @return
     */
    public static ResponseResult error(String msg){
        if(msg == null){
            msg = ResponseCodeEnum.ERROR.getMsg();
        }
        return new ResponseResult(ResponseCodeEnum.ERROR.getCode(),msg,null);
    }

    /**
     * 失败 返回“错误”
     * @return
     */
    public static ResponseResult error(){
        return new ResponseResult(ResponseCodeEnum.ERROR.getCode(),ResponseCodeEnum.ERROR.getMsg(),null);
    }

    /**
     * 登录信息过期
     * @return
     */
    public static ResponseResult tokenExpire(){
        return new ResponseResult(ResponseCodeEnum.TOKEN_EXPIRE.getCode(),ResponseCodeEnum.TOKEN_EXPIRE.getMsg(),null);
    }

    /**
     * 没有权限
     * @return
     */
    public static ResponseResult noPermission(){
        return new ResponseResult(ResponseCodeEnum.NO_PERMISSION.getCode(),ResponseCodeEnum.NO_PERMISSION.getMsg(),null);
    }

    /**
     * 非法请求
     * @return
     */
    public static ResponseResult requestIllegal(){
        return new ResponseResult(ResponseCodeEnum.REQUEST_ILLEGAL.getCode(),ResponseCodeEnum.REQUEST_ILLEGAL.getMsg(),null);
    }

    /**
     * 缺少参数
     * @return
     */
    public static ResponseResult parameterLack(){
        return new ResponseResult(ResponseCodeEnum.PARAMETER_LACK.getCode(),ResponseCodeEnum.PARAMETER_LACK.getMsg(),null);
    }

    /**
     * 参数值不允许
     * @return
     */
    public static ResponseResult parameterIllegal(){
        return new ResponseResult(ResponseCodeEnum.PARAMETER_ILLEGAL.getCode(),ResponseCodeEnum.PARAMETER_ILLEGAL.getMsg(),null);
    }


    /**
     * 是否成功
     * @return
     */
    public boolean isSuccess(){
        return this.code == ResponseCodeEnum.SUCCESS.getCode();
    }
}

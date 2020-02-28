package cn.yang.tmall.common;

import lombok.Data;

/**
 * @author Yangtz
 * @ClassName: ResponseCode
 * @Description:
 * @create 2020-01-20 15:51
 */
public enum ResponseCode {
    SUCCESS(1001,"SUCCESS"),
    ERROR(1002,"ERROR"),
    /**
     * 1100-2099为登录信息状态码
     */
    NEED_LOGIN(1100,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(1101,"ILLEGAL_ARGUMENT");

    /**
     * 2100-3099为商品搜索状态码
     */


    private final int code;
    private final String msg;

    ResponseCode(int code,String msg){
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

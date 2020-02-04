package cn.yang.tmall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

/**
 * @author Yangtz
 * @ClassName: ServerResponse
 * @Description: 通用返回对象
 * @create 2020-01-20 14:37
 */
@JsonInclude(value = Include.NON_NULL)
public class RestTO<T> implements Serializable {
    private int status;
    private String msg;
    private T data;
    private RestTO(int status){
        this.status = status;
    }
    private RestTO(int status, T data){
        this.status = status;
        this.data = data;
    }
    private RestTO(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    private RestTO(int status, String msg){
        this.status = status;
        this.msg = msg;
    }
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> RestTO<T> success(){
        return new RestTO<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> RestTO<T> success(String msg){
        return new RestTO<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> RestTO<T> success(T data){
        return new RestTO<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> RestTO<T> success(String msg, T data){
        return new RestTO<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public static <T> RestTO<T> error(){
        return new RestTO<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getMsg());
    }
    public static <T> RestTO<T> error(String errorMsg){
        return new RestTO<T>(ResponseCode.ERROR.getCode(),errorMsg);
    }
    public static <T> RestTO<T> error(int errorCode, String errorMsg){
        return new RestTO<T>(errorCode,errorMsg);
    }
}

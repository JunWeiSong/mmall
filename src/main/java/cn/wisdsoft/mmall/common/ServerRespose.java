package cn.wisdsoft.mmall.common;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
/**
* @Description:  TODO
* @Author:  宋军伟
* @CreateDate:  2018/10/14 18:09
* @Version:  1.0
*/
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候，如果是null的对象，key也会消失
public class ServerRespose<T> implements Serializable {
    //状态
    private int status;
    //消息
    private String msg;
    //返回数据
    private T data;

    private ServerRespose(int status){
        this.status=status;
    }
    private ServerRespose(int status, T data){
        this.status=status;
        this.data=data;
    }
    private ServerRespose(int status , String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerRespose(int status, String msg, T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

    @JsonIgnore
    //使之不在json序列化到结果当中
    public boolean isSuccess(){
        //如果当前的状态=0，则返回成功
        return this.status == ResposeCode.SUCCESS.getCode();
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
    //不传参时返回一个code=1
    public static <T> ServerRespose<T> CreateBySuccess(){
        return  new ServerRespose<T>(ResposeCode.SUCCESS.getCode());
    }

    public static <T> ServerRespose<T> CreateBySuccessMessage(String msg){
        return  new ServerRespose<T>(ResposeCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerRespose<T> CreateBySuccess(T data){
        return  new ServerRespose<T>(ResposeCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerRespose<T> CreateBySuccess(String msg, T data){
        return  new ServerRespose<T>(ResposeCode.SUCCESS.getCode(),msg,data);
    }
    public static <T> ServerRespose<T> CreateByError(){
        return  new ServerRespose<T>(ResposeCode.ERROR.getCode());
    }

    public static <T> ServerRespose<T> CreateByErrorMessage(String ErrorMessage){
        return  new ServerRespose<T>(ResposeCode.ERROR.getCode(),ErrorMessage);
    }
    public static <T> ServerRespose<T> CreateByErrorCodeMessage(int Code,String ErrorMessage){
        return  new ServerRespose<T>(Code,ErrorMessage);
    }
}

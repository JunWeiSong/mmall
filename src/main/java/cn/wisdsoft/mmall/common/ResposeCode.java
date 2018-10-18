package cn.wisdsoft.mmall.common;

public enum ResposeCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    //需要登录
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;
    ResposeCode(int code,String desc){
        this.code=code;
        this.desc=desc;
    }
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

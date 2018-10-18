package cn.wisdsoft.mmall.common;

/**
* @Description:  TODO
* @Author:  宋军伟
* @CreateDate:  2018/10/15 9:44
* @Version:  1.0
*/
public class Const {
    /**
     * 用户的session信息
     */
    public static final String CURRENT_USER = "current_user";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface Role{
        int ROLE_CUSTOMER=0; //普通用户
        int ROLE_ADMIN=1;//管理员
    }
}

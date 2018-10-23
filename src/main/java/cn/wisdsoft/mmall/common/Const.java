package cn.wisdsoft.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

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

    public interface productByDescOrAsc{
        Set<String> Price_Asc_Desc = Sets.newHashSet("price_desc","price_asc");
    }

    public interface Role{
        int ROLE_CUSTOMER=0; //普通用户
        int ROLE_ADMIN=1;//管理员
    }

    public enum productStatusEnum{
        ON_SALE(1,"在线");
        private int code;
        private String value;
        productStatusEnum(int code,String value){
            this.code=code;
            this.value=value;
        }
        public int getCode() {
            return code;
        }
        public String getValue() {
            return value;
        }
    }
}

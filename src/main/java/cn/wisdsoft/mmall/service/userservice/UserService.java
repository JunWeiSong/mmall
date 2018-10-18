package cn.wisdsoft.mmall.service.userservice;

import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.pojo.MmallUser;

import javax.servlet.http.HttpSession;

/**
* @Description:  TODO
* @Author:  宋军伟
* @CreateDate:  2018/10/14 16:51
* @Version:  1.0
*/
public interface UserService {

    ServerRespose<MmallUser> login(String username, String password);

    ServerRespose<String> checkRegister(MmallUser mmallUser);

    ServerRespose<String> checkValid(String str,String type);

    ServerRespose<String> forgetAnswer(String username,String question,String answer);

    ServerRespose<String> forgetResterPassword(String username,String passwordNew,String forgetToken);

    ServerRespose<String> resterPassword(String passwordOld,String passwordNew,MmallUser mmallUser);

    ServerRespose<MmallUser> resterUserinfo(MmallUser mmallUser);

    ServerRespose checkAdminRole(MmallUser mmallUser);
}

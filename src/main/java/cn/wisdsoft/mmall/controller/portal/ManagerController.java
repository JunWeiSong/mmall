package cn.wisdsoft.mmall.controller.portal;

import cn.wisdsoft.mmall.common.Const;
import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.pojo.MmallUser;
import cn.wisdsoft.mmall.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Description: TODO
 * @Author:  songjunwei
 * @CreateDate: 2018/10/15 20:46
 * @Version: 1.0
 */
@Controller
@RequestMapping("/manager/user")
public class ManagerController {
    @Autowired
    private UserService userService;

    /**
    * @Description: 管理员登陆
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 20:50
    */
    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<MmallUser> login(String username, String password, HttpSession session){
        ServerRespose<MmallUser> login = userService.login(username, password);
        if(login.isSuccess()){
            MmallUser loginData = login.getData();
            if(loginData.getRole()== Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENT_USER,loginData);
                return login;
            }else {
                return ServerRespose.CreateByErrorMessage("非管理员无法登陆");
            }
        }
        return login;
    }
}

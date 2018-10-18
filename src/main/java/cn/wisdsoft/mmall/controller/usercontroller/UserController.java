package cn.wisdsoft.mmall.controller.usercontroller;

import cn.wisdsoft.mmall.common.Const;
import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.pojo.MmallUser;
import cn.wisdsoft.mmall.service.userservice.UserService;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletSecurityElement;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
import java.io.UnsupportedEncodingException;
import java.rmi.server.RemoteServer;

/**
* @Description:  TODO
* @Author:  宋军伟
* @CreateDate:  2018/10/14 15:43
* @Version:  1.0
*/
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * @Description:
     * @Author:  宋军伟
     * @CreateDate:  2018/10/14 16:45
     * @param username 用户名
     * @param password 密码
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<MmallUser> login(String username, String password, HttpSession session){
        ServerRespose<MmallUser> login = userService.login(username, password);
        if(login.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, login.getData());
        }
        return login;
    }
    /**
    * @Description:  退出登录,清楚session
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 9:19
     * @param
     * @return : null
    */
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerRespose.CreateBySuccess();
    }
    /**
    * @Description:  用户注册
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 10:08
     * @param
     * @return : null
    */
    @RequestMapping(value = "checkRegister.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<String> checkRegister(MmallUser mmallUser){
        return  userService.checkRegister(mmallUser);
    }

    /**
    * @Description: 校验用户输入的是用户名还是Email
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 10:35
     * @param
     * @return : null
    */
    @RequestMapping(value = "checkValid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<String> checkValid(String str,String type){
        return userService.checkValid(str,type);
    }
    /**
    * @Description:  查看用户是否登陆
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 10:44
     * @param
     * @return : null
    */
    @RequestMapping(value = "getuserinfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<MmallUser> getuserinfo(HttpSession session){
        MmallUser sessionAttribute = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(sessionAttribute==null){
            return ServerRespose.CreateByErrorMessage("用户未登陆，无法获取登陆信息");
        }
        return ServerRespose.CreateBySuccess(sessionAttribute);
    }
    /**
    * @Description: 忘记密码问题的答案检查
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 16:37
     * @param
     * @return : null
    */
    @RequestMapping(value = "forgetAnswer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<String> forgetAnswer(String username,String question,String answer){
        return userService.forgetAnswer(username, question, answer);
    }
    /**
    * @Description: 忘记密码中的重置密码
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 17:59
     * @param
     * @return : null
    */
    @RequestMapping(value = "forgetResterPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<String> forgetResterPassword(String username,String passwordNew,String forgetToken){
        return userService.forgetResterPassword(username, passwordNew, forgetToken);
    }
    /**
    * @Description:  登陆状态的重置密码
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 18:03
     * @param
     * @return : null
    */
    @RequestMapping(value = "resterPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<String> resterPassword(HttpSession session,String passwordOld,String passwordNew){
        MmallUser mmalluser = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(mmalluser==null){
            return ServerRespose.CreateByErrorMessage("用户未登陆");
        }
        return userService.resterPassword(passwordOld,passwordNew,mmalluser);
    }
    /**
    * @Description: 更新用户信息
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 19:39
     * @param
     * @return : null
    */
    @RequestMapping(value = "resterUserinfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<MmallUser> resterUserinfo(HttpSession session,MmallUser user) throws UnsupportedEncodingException {
        MmallUser mUser = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(mUser==null){
            return ServerRespose.CreateByErrorMessage("用户未登陆");
        }
        //在session中获取ID以及name，防止纵向越权
        user.setId(mUser.getId());
        user.setUsername(mUser.getUsername());
        if(StringUtils.isBlank(user.getQuestion())){
            user.setQuestion(mUser.getQuestion());
        }
        if(StringUtils.isBlank(user.getAnswer())) {
            user.setAnswer(mUser.getAnswer());
        }
        if(StringUtils.isBlank(user.getEmail())) {
            user.setEmail(mUser.getEmail());
        }
        if(StringUtils.isBlank(user.getPhone())) {
            user.setPhone(mUser.getPhone());
        }
        user.setRole(mUser.getRole());
        ServerRespose<MmallUser> userServerRespose = userService.resterUserinfo(user);
        if(userServerRespose.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,userServerRespose.getData());
        }
        return userServerRespose;
    }
}

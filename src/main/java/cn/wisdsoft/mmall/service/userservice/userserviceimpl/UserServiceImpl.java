package cn.wisdsoft.mmall.service.userservice.userserviceimpl;

import cn.wisdsoft.mmall.common.Const;
import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.common.TokenCache;
import cn.wisdsoft.mmall.mapper.MmallUserMapper;
import cn.wisdsoft.mmall.pojo.MmallUser;
import cn.wisdsoft.mmall.service.userservice.UserService;
import cn.wisdsoft.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private MmallUserMapper usermapper;

    /**
    * @Description:    用户登录
    * @Author:  宋军伟
    * @CreateDate:  2018/10/14 19:47
    */
    @Override
    public ServerRespose<MmallUser> login(String username, String password) {
        int id = usermapper.checkUsername(username);
        if(id==0){
            return ServerRespose.CreateByErrorMessage("用户名错误");
        }
        //TODO md5加密
        String md5password = MD5Util.MD5EncodeUtf8(password);
        MmallUser user = usermapper.selectLogin(username,md5password);
        if(user==null){
            return ServerRespose.CreateByErrorMessage("密码错误");
        }
        //将密码置为空
        user.setPassword(StringUtils.EMPTY);
        return ServerRespose.CreateBySuccess("登录成功",user);
    }

    /**
    * @Description:  注册账号
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 9:35
    */
    @Override
    public ServerRespose<String> checkRegister(MmallUser mmallUser){
        ServerRespose<String> validResult = this.checkValid(mmallUser.getUsername(), Const.USERNAME);
        if(!validResult.isSuccess()){
            return validResult;
        }
        validResult = this.checkValid(mmallUser.getEmail(),Const.EMAIL);
        if(!validResult.isSuccess()){
            return validResult;
        }
        mmallUser.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        mmallUser.setPassword(MD5Util.MD5EncodeUtf8(mmallUser.getPassword()));
        int count = usermapper.insert(mmallUser);
        if(count==0){
            return ServerRespose.CreateByErrorMessage("注册失败");
        }
        return ServerRespose.CreateBySuccessMessage("注册成功");
    }
    /**
    * @Description: 校验用户以什么方式登陆（username or email）
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 10:48
    */
    public ServerRespose<String> checkValid(String str,String type){
        //校验
        if(StringUtils.isNotBlank(type)){
            if(Const.USERNAME.equals(type)){
                int count = usermapper.checkUsername(str);
                if(count>0){
                    return ServerRespose.CreateByErrorMessage("用户名已存在");
                }
                if(Const.EMAIL.equals(type)){
                    count = usermapper.checkEmail(str);
                    if(count>0){
                        return ServerRespose.CreateByErrorMessage("Email已存在");
                    }
                }
            }else {
                return ServerRespose.CreateByErrorMessage("参数错误");
            }
        }
        return ServerRespose.CreateBySuccessMessage("校验成功");
    }
    /**
    * @Description:   找回密码
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 11:06
    */
    public ServerRespose<String> forgetGetQuestion(String username){
        ServerRespose<String> stringServerRespose = this.checkValid(username, Const.USERNAME);
        if(stringServerRespose.isSuccess()){
            //用户不存在 （取非）
            return ServerRespose.CreateBySuccessMessage("用户不存在");
        }
        String selectQuestionByusername = usermapper.selectQuestionByusername(username);
        if(StringUtils.isNotBlank(selectQuestionByusername)){
            return ServerRespose.CreateBySuccess(selectQuestionByusername);
        }
        return ServerRespose.CreateByErrorMessage("找回密码的问题为空");
    }
    /**
    * @Description:  该用户的问题答案是否正确
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 17:41
    */
    public ServerRespose<String> forgetAnswer(String username,String question,String answer){
        int count = usermapper.checkAnswer(username,question,answer);
        //说明此用户的问题及答案是正确的
        if(count>0){
            //生成一个几乎不可重复的字符串
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerRespose.CreateBySuccess(forgetToken);
        }
        return ServerRespose.CreateByErrorMessage("问题的答案错误");
    }
    /**
    * @Description: 修改密码
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 17:50
     * @param username,passwordNew,forgetToken
     * @return : null
    */
    public ServerRespose<String> forgetResterPassword(String username,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerRespose.CreateByErrorMessage("没有Token,请获取Token");
        }
        ServerRespose<String> stringServerRespose = this.checkValid(username, Const.USERNAME);
        if(stringServerRespose.isSuccess()){
            //用户不存在 （取非）
            return ServerRespose.CreateBySuccessMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerRespose.CreateByErrorMessage("Token无效或者过期");
        }
        if(StringUtils.equals(forgetToken,token)){
            String MD5passwordNew = MD5Util.MD5EncodeUtf8(passwordNew);
            int count = usermapper.updatePasswordByusername(username,MD5passwordNew);
            if(count>0){
                return ServerRespose.CreateBySuccessMessage("修改密码成功");
            }
        }else{
                return ServerRespose.CreateByErrorMessage("token错误，请重新获取token");
        }
        return ServerRespose.CreateByErrorMessage("修改密码失败");
    }
    /**
    * @Description: 登陆状态更改密码
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 19:42
    */
    public ServerRespose<String> resterPassword(String passwordOld,String passwordNew,MmallUser mmallUser){
        int count = usermapper.checkpasswordold(MD5Util.MD5EncodeUtf8(passwordOld),mmallUser.getId());
        if(count==0){
            return ServerRespose.CreateByErrorMessage("旧密码输入错误");
        }
        mmallUser.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updeteCount = usermapper.updateByPrimaryKeySelective(mmallUser);
        if(updeteCount>0){
            return ServerRespose.CreateBySuccessMessage("修改成功");
        }
        return ServerRespose.CreateByErrorMessage("修改密码失败");
    }

    /**
    * @Description: 更新用户信息
    * @Author:  宋军伟
    * @CreateDate:  2018/10/15 19:43
    */
    public ServerRespose<MmallUser> resterUserinfo(MmallUser mmallUser){
        //username不可以被更新
        //email更新，需要判断是否存在相同的email，相同则更新失败
        int count = usermapper.checkExitEmail(mmallUser.getEmail(),mmallUser.getId());
        if(count>0){
            return ServerRespose.CreateByErrorMessage("Email已存在，请更改");
        }
        //要更新的字段
        MmallUser user = new MmallUser();
        user.setId(mmallUser.getId());
        user.setEmail(mmallUser.getEmail());
        user.setQuestion(mmallUser.getQuestion());
        user.setPhone(mmallUser.getPhone());
        user.setAnswer(mmallUser.getAnswer());
        int updateCount = usermapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerRespose.CreateBySuccess("更新个人信息成功",mmallUser);
        }
        return ServerRespose.CreateByErrorMessage("更新个人信息失败");
    }

    /**
    * @Description: 查看是否是管理员
    * @Author:  宋军伟
    * @CreateDate:  2018/10/16 21:53
    */
    public ServerRespose checkAdminRole(MmallUser mmallUser){
        //TODO intValue()是什么意思
        if(mmallUser!=null && mmallUser.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServerRespose.CreateBySuccess();
        }
        return ServerRespose.CreateByErrorMessage("不是管理员，没有权限");
    }
}

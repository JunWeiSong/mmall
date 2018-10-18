package cn.wisdsoft.mmall.controller.usercontroller;

import cn.wisdsoft.mmall.common.Const;
import cn.wisdsoft.mmall.common.ResposeCode;
import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.pojo.MmallUser;
import cn.wisdsoft.mmall.service.categoryservice.CategoryService;
import cn.wisdsoft.mmall.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
* @Description:  TODO
* @Author:  宋军伟
* @CreateDate:  2018/10/16 20:19
* @Version:  1.0
*/
@Controller
@RequestMapping("/manager/category")
public class CategoryManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    /**
    * @Description: 增加产品
    * @Author:  宋军伟
    * @CreateDate:  2018/10/16 21:59
     * @param
     * @return : null
    */
    @RequestMapping(value = "/insertCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose insertCategory(HttpSession session,String CategoryName,@RequestParam(value = "parentId",defaultValue ="0") int ParentId){
        MmallUser mmallUser = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(mmallUser==null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(), "用户未登陆请登陆");
        }
        ServerRespose adminRole = userService.checkAdminRole(mmallUser);
        //是管理员
        if(adminRole.isSuccess()){
            //业务逻辑
            return categoryService.insertCategory(CategoryName, ParentId);
        }else {
            return adminRole;
        }
    }
    /**
    * @Description: 更新商品名称
    * @Author:  宋军伟
    * @CreateDate:  2018/10/16 22:38
    */
    @RequestMapping(value = "/updateCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose updateCategory(HttpSession session,String CategoryName,Integer CategoryId){
        MmallUser mmallUser = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(mmallUser==null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(), "用户未登陆请登陆");
        }
        ServerRespose adminRole = userService.checkAdminRole(mmallUser);
        //检查是否是管理员
        if(adminRole.isSuccess()){
            //更新CategoryName
            return categoryService.updateCategory(CategoryName, CategoryId);
        }else {
            return adminRole;
        }
    }
    /**
    * @Description: 查询Category 子节点的所有平级商品
    * @Author:  宋军伟
    * @CreateDate:  2018/10/16 22:43
     * @return : null
    */
    @RequestMapping(value = "/selectchildparallelCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose selectchildparallelCategory(HttpSession session,@RequestParam(value = "CategoryId",defaultValue = "0") Integer CategoryId){
        MmallUser mmallUser = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(mmallUser==null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(), "用户未登陆请登陆");
        }
        ServerRespose adminRole = userService.checkAdminRole(mmallUser);
        //检查是否是管理员
        if(adminRole.isSuccess()){
            //查询CategoryName子节点的平级商品，不递归
            return categoryService.selectchildparallelCategory( CategoryId);
        }else {
            return adminRole;
        }
    }
    /**
    * @Description: 查询当前节点id和递归节点的ID
    * @Author:  宋军伟
    * @CreateDate:  2018/10/17 19:58
    */
    @RequestMapping(value = "/selectChildCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose selectCategoryAndDeepchidCategroy(HttpSession session,@RequestParam(value = "CategoryId",defaultValue = "0") Integer CategoryId){
        MmallUser mmallUser = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(mmallUser==null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(), "用户未登陆请登陆");
        }
        ServerRespose adminRole = userService.checkAdminRole(mmallUser);
        //检查是否是管理员
        if(adminRole.isSuccess()){
            //查询当前节点的id，以及子节点的、id,递归
            return categoryService.selectchildparallelCategory( CategoryId);
        }else {
            return adminRole;
        }
    }
}

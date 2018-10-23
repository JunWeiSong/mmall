package cn.wisdsoft.mmall.controller.portal;
import cn.wisdsoft.mmall.common.Const;
import cn.wisdsoft.mmall.common.ResposeCode;
import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.pojo.MmallProduct;
import cn.wisdsoft.mmall.pojo.MmallUser;
import cn.wisdsoft.mmall.service.fileservice.FileService;
import cn.wisdsoft.mmall.service.productservice.ProductService;
import cn.wisdsoft.mmall.service.userservice.UserService;
import cn.wisdsoft.mmall.util.PropertiesUtil;
import cn.wisdsoft.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class ProductManageController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;

    /**
     * @Description:增加及更新商品
     * @Author:  宋军伟
     * @CreateDate:  2018/10/18 22:47
     */
    @RequestMapping(value = "insertAndUpdateProduct.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose insertProduct(HttpSession session, MmallProduct product){
        MmallUser attribute = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(attribute == null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆管理员");
        }
        ServerRespose respose = userService.checkAdminRole(attribute);
        if(respose.isSuccess()){
            //增加产品
            return productService.insertandupdateProduct(product);
        }else{
            return respose;
        }
    }
    /**
    * @Description: 更新商品状态
    * @Author:  宋军伟
    * @CreateDate:  2018/10/18 22:48
    */
    @RequestMapping(value = "updateStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<String> updateStatus(HttpSession session,Integer productId,Integer status){
        MmallUser attribute = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(attribute == null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆管理员");
        }
        ServerRespose respose = userService.checkAdminRole(attribute);
        if(respose.isSuccess()){
            //更新商品状态
            return productService.updateStatus(productId,status);
        }else{
            return respose;
        }
    }
    /**
    * @Description:  产品详情
    * @Author:  宋军伟
    * @CreateDate:  2018/10/18 23:00
    */
    @RequestMapping(value = "selectDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<ProductDetailVo> selectDetail(HttpSession session, Integer productId){
        MmallUser attribute = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(attribute == null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆管理员");
        }
        ServerRespose respose = userService.checkAdminRole(attribute);
        if(respose.isSuccess()){
            //查询商品详情
            return productService.selectDetail(productId);
        }else{
            return respose;
        }
    }

    /**
    * @Description:
    * @Author:  宋军伟
    * @CreateDate:  2018/10/19 13:53
     * @param
     * @return : null
    */
    @RequestMapping(value = "selectList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<PageInfo> selectList(HttpSession session,@RequestParam(value ="pageNum",defaultValue = "1") int pageNum,@RequestParam(value ="pageSize",defaultValue = "10")int pageSize){
        MmallUser attribute = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(attribute == null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆管理员");
        }
        ServerRespose respose = userService.checkAdminRole(attribute);
        if(respose.isSuccess()){
            //查询
            return productService.selectList(pageNum, pageSize);
        }else{
            return respose;
        }
    }
    /**
    * @Description:  根据产品名称以及产品编号查询
    * @Author:  宋军伟
    * @CreateDate:  2018/10/20 21:11
     * @param
     * @return : null
    */
    @RequestMapping(value = "ProductSearch.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<PageInfo> selectProductSearch(HttpSession session,String ProductName,Integer ProductId,@RequestParam(value ="pageNum",defaultValue = "1") int pageNum,@RequestParam(value ="pageSize",defaultValue = "10")int pageSize){
        MmallUser attribute = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(attribute == null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆管理员");
        }
        ServerRespose respose = userService.checkAdminRole(attribute);
        if(respose.isSuccess()){
            //查询
            return productService.selectProductByNameOrId(ProductName,ProductId,pageNum,pageSize);
        }else{
            return respose;
        }
    }
    /**
    * @Description: 上传图片
    * @Author:  宋军伟
    * @CreateDate:  2018/10/20 22:40
     * @param
     * @return : null
    */
    @RequestMapping(value = "uploadimg.do")
    @ResponseBody
    public ServerRespose uploadimg(HttpSession session,MultipartFile file , HttpServletRequest request){
        MmallUser attribute = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(attribute == null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆管理员");
        }
        ServerRespose respose = userService.checkAdminRole(attribute);
        if(respose.isSuccess()){
            //得到项目的绝对路径
            String uploadpath = request.getSession().getServletContext().getRealPath("updload");
            String targetFileName = fileService.upload(file, uploadpath);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

            Map hashMap = Maps.newHashMap();
            hashMap.put("uri",targetFileName);
            hashMap.put("url",url);
            return ServerRespose.CreateBySuccess(hashMap);
        }else{
            return respose;
        }
    }
    /**
    * @Description:  simditor富文本编辑器 的图片上传
    * @Author:  宋军伟
    * @CreateDate:  2018/10/20 23:10
     * @param
     * @return : null
    */
    @RequestMapping(value = "richTextImgUpload.do")
    @ResponseBody
    public Map richTextImgUpload(HttpSession session, MultipartFile file , HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        MmallUser attribute = (MmallUser) session.getAttribute(Const.CURRENT_USER);
        if(attribute == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        ServerRespose respose = userService.checkAdminRole(attribute);
        if(respose.isSuccess()){
            //得到项目的绝对路径
            String uploadpath = request.getSession().getServletContext().getRealPath("updload");
            //上传后的文件名
            String targetFileName = fileService.upload(file, uploadpath);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
//            如果Header中没有定义则添加，如果已定义则保持原有value不改变。
            response.addHeader("Access-control-Allow-header","X-File-Name");
            return resultMap;
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }
}

package cn.wisdsoft.mmall.controller.usercontroller;

import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.service.productservice.ProductService;
import cn.wisdsoft.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.net.MalformedServerReplyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
* @Description:  前台产品借口
* @Author:  宋军伟
* @CreateDate:  2018/10/21 10:29
* @Version:  1.0
*/
@Controller
@RequestMapping("/product")
public class ProductUserController {
    @Autowired
    private ProductService productService;

    /**
    * @Description: 查询商品详情
    * @Author:  宋军伟
    * @CreateDate:  2018/10/21 11:10
     * @param
     * @return : null
    */
    @RequestMapping(value = "/selectDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerRespose<ProductDetailVo> selectDetail(Integer productId){
        return productService.selectProductDetail(productId);
    }

    /**
    * @Description: 根据name（可以不传） and Categoryid（可以不传）and orderby(可以不传) 查询数据，并分页
    * @Author:  宋军伟
    * @CreateDate:  2018/10/21 14:02
    */
    @RequestMapping(value = "/selectProByNameAndCategoryId.do",method = RequestMethod.POST)
    @ResponseBody
    ServerRespose<PageInfo> selectProByNameAndCategoryId( @RequestParam(value ="productName",required = false) String productName,
                                                          @RequestParam(value ="CategoryId",required = false) Integer CategoryId,
                                                          @RequestParam(value ="PageNum",defaultValue = "1") int pageNum,
                                                          @RequestParam(value ="PageSize",defaultValue = "10") int PageSize,
                                                          @RequestParam(value ="orderBy",defaultValue = "") String orderBy){
        return productService.selectProByNameAndCategoryId(productName,CategoryId,pageNum,PageSize,orderBy);
    }
}

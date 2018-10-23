package cn.wisdsoft.mmall.service.productservice;

import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.pojo.MmallProduct;
import cn.wisdsoft.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

public interface ProductService {
    ServerRespose insertandupdateProduct(MmallProduct product);

    ServerRespose<String> updateStatus(Integer productId,Integer status);

    ServerRespose<ProductDetailVo> selectDetail(Integer productId);

    ServerRespose<PageInfo> selectList(int pageNum, int pageSize);

    ServerRespose<PageInfo> selectProductByNameOrId(String ProductName,Integer ProductId,int pageNum,int pageSize);

    ServerRespose<ProductDetailVo> selectProductDetail(Integer productId);

    ServerRespose<PageInfo> selectProByNameAndCategoryId(String productName,Integer CategoryId,int pageNum,int PageSize,String orderBy);
}

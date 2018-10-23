package cn.wisdsoft.mmall.service.productservice.productserviceimpl;

import cn.wisdsoft.mmall.common.Const;
import cn.wisdsoft.mmall.common.ResposeCode;
import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.mapper.MmallCategoryMapper;
import cn.wisdsoft.mmall.mapper.MmallProductMapper;
import cn.wisdsoft.mmall.pojo.MmallCategory;
import cn.wisdsoft.mmall.pojo.MmallProduct;
import cn.wisdsoft.mmall.service.categoryservice.CategoryService;
import cn.wisdsoft.mmall.service.productservice.ProductService;
import cn.wisdsoft.mmall.util.DateTimeUtil;
import cn.wisdsoft.mmall.util.PropertiesUtil;
import cn.wisdsoft.mmall.vo.ProductDetailVo;
import cn.wisdsoft.mmall.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private MmallProductMapper productMapper;

    @Autowired
    private MmallCategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;
    @Override
    public ServerRespose insertandupdateProduct(MmallProduct product) {
        if(product !=null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subimgsArray = product.getSubImages().split(",");
                if(subimgsArray.length>0){
                    product.setMainImage(subimgsArray[0]);
                }
            }
        }
        if(product.getId() !=null){
            int update = productMapper.updateByPrimaryKeySelective(product);
            if(update>0){
                return ServerRespose.CreateBySuccessMessage("更新商品成功");
            }else{
                return ServerRespose.CreateBySuccessMessage("更新商品失败");
            }
        }else{
            int insertPro = productMapper.insert(product);
            if(insertPro>0){
                return ServerRespose.CreateBySuccessMessage("新增成功");
            }
            return ServerRespose.CreateBySuccessMessage("新增失败");
        }
    }

    public ServerRespose<String> updateStatus(Integer productId,Integer status){
        if(productId==null || status == null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.ILLEGAL_ARGUMENT.getCode(),ResposeCode.ILLEGAL_ARGUMENT.getDesc());
        }
        MmallProduct product = new MmallProduct();
        product.setId(productId);
        product.setStatus(status);
        int rowcount = productMapper.updateByPrimaryKeySelective(product);
        if(rowcount>0){
            return ServerRespose.CreateBySuccessMessage("修改产品的销售状态成功！");
        }
        return ServerRespose.CreateByErrorMessage("修改产品的销售状态失败");
    }
    public ServerRespose<ProductDetailVo> selectDetail(Integer productId){
        if(productId == null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.ILLEGAL_ARGUMENT.getCode(),ResposeCode.ILLEGAL_ARGUMENT.getDesc());
        }
        MmallProduct product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerRespose.CreateByErrorMessage("该商品已下架或删除");
        }
        // vo对象
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerRespose.CreateBySuccess(productDetailVo);
    }

    // vo对象
    private ProductDetailVo assembleProductDetailVo(MmallProduct product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        MmallCategory category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    public ServerRespose<PageInfo> selectList(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<MmallProduct> mmallProducts = productMapper.selectList();

        ArrayList<ProductListVo> productListVoArrayList = Lists.newArrayList();
        for (MmallProduct productItem : mmallProducts) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoArrayList.add(productListVo);
        }
        //前台所需要的不是全部的product数据，而是部分，但是又需要用数据进行分页
        PageInfo pageresult = new PageInfo<>(mmallProducts);
        //所以需要将数据重置，将数据传到vo对象中
        pageresult.setList(productListVoArrayList);
        return ServerRespose.CreateBySuccess(pageresult);

    }
    private ProductListVo assembleProductListVo(MmallProduct product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    public ServerRespose<PageInfo> selectProductByNameOrId(String ProductName,Integer ProductId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<MmallProduct> mmallProducts = productMapper.selectProductByNameOrId(ProductName, ProductId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (MmallProduct product :mmallProducts){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo<>(mmallProducts);
        pageInfo.setList(productListVoList);
        return ServerRespose.CreateBySuccess(pageInfo);
    }

    public ServerRespose<ProductDetailVo> selectProductDetail(Integer productId){
        if(productId==null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.ILLEGAL_ARGUMENT.getCode(),ResposeCode.ILLEGAL_ARGUMENT.getDesc());
        }
        MmallProduct product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerRespose.CreateByErrorMessage("该产品已经下架或删除");
        }
        if(product.getStatus()!= Const.productStatusEnum.ON_SALE.getCode()){
            return ServerRespose.CreateByErrorMessage("该产品已经下架或删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerRespose.CreateBySuccess(productDetailVo);
    }

    public ServerRespose<PageInfo> selectProByNameAndCategoryId(String productName,Integer CategoryId,int pageNum,int PageSize,String orderBy){
        if(StringUtils.isBlank(productName) && CategoryId==null){
            return ServerRespose.CreateByErrorCodeMessage(ResposeCode.ILLEGAL_ARGUMENT.getCode(),ResposeCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();
        if(CategoryId!=null){
            MmallCategory category = categoryMapper.selectByPrimaryKey(CategoryId);
            if(category ==null && StringUtils.isBlank(productName)){
                //若查询的分类为空，且传的产品名称也为空，返回一个空结果集
                PageHelper.startPage(pageNum,PageSize);
                ArrayList<ProductListVo> productListVos = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVos);
                return ServerRespose.CreateBySuccess(pageInfo);
            }
            categoryIdList = categoryService.selectChildCategory(category.getId()).getData();
        }
        PageHelper.startPage(pageNum,PageSize);
        if(StringUtils.isNotBlank(orderBy)){
            String[] orderByArray = orderBy.split("_");
            PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
        }
        List<MmallProduct> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(productName)?null:productName,categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(MmallProduct product : productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerRespose.CreateBySuccess(pageInfo);
    }
}

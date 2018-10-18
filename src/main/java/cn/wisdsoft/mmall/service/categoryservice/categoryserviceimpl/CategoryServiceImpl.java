package cn.wisdsoft.mmall.service.categoryservice.categoryserviceimpl;

import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.controller.usercontroller.CategoryManagerController;
import cn.wisdsoft.mmall.mapper.MmallCategoryMapper;
import cn.wisdsoft.mmall.pojo.MmallCategory;
import cn.wisdsoft.mmall.service.categoryservice.CategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private MmallCategoryMapper mmallCategoryMapper;

    public ServerRespose insertCategory(String Categoryname,Integer parentId){
        if(parentId==null || StringUtils.isBlank(Categoryname)){
            return ServerRespose.CreateByErrorMessage("添加商品参数错误");
        }
        MmallCategory category = new MmallCategory();
        category.setName(Categoryname);
        category.setParentId(parentId);
        category.setStatus(true);
        int resultCount = mmallCategoryMapper.insert(category);
        if(resultCount>0){
            return ServerRespose.CreateBySuccessMessage("添加商品成功");
        }
        return ServerRespose.CreateByErrorMessage("添加商品失败");
    }

    @Override
    public ServerRespose updateCategory(String CategoryName, Integer CategoryId) {
        if(CategoryId==null || StringUtils.isBlank(CategoryName)){
            return ServerRespose.CreateByErrorMessage("更新商品参数错误");
        }
        MmallCategory category = new MmallCategory();
        category.setId(CategoryId);
        category.setName(CategoryName);
        int count = mmallCategoryMapper.updateByPrimaryKeySelective(category);
        if(count>0){
            return ServerRespose.CreateBySuccessMessage("更新商品名称成功");
        }
        return ServerRespose.CreateByErrorMessage("商品更新失败");
    }

    @Override
    public ServerRespose<List<MmallCategory>> selectchildparallelCategory(Integer CategoryId) {
        List<MmallCategory> mmallCategories = mmallCategoryMapper.selectChildrenByParentId(CategoryId);
        // CollectionUtils使用：isEmpty()是判断空字符串和空集合
        if(CollectionUtils.isEmpty(mmallCategories)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerRespose.CreateBySuccess(mmallCategories);
    }
    @Override
    public ServerRespose<List<Integer>> selectChildCategory(Integer CategoryId) {
        //初始化
        Set<MmallCategory> categorySet = Sets.newHashSet();
        findChidCategory(categorySet,CategoryId);

        List<Integer> categoryidlist = Lists.newArrayList();
        if(categoryidlist != null){
            for(MmallCategory categoryItem :categorySet){
                categoryidlist.add(categoryItem.getId());
            }
        }
        return ServerRespose.CreateBySuccess(categoryidlist);
    }

    //递归算法，算出子节点
    private Set<MmallCategory>  findChidCategory(Set<MmallCategory> categorySet,Integer Categoryid){
        MmallCategory category = mmallCategoryMapper.selectByPrimaryKey(Categoryid);
        if(category!=null){
            categorySet.add(category);
        }
        //查找子节点，递归要有一个退出条件
        List<MmallCategory> categorieList = mmallCategoryMapper.selectChildrenByParentId(Categoryid);
        for (MmallCategory category1: categorieList){
            findChidCategory(categorySet,category1.getId());
        }
        return categorySet;
    }
}

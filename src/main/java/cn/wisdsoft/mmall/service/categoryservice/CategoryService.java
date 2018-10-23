package cn.wisdsoft.mmall.service.categoryservice;

import cn.wisdsoft.mmall.common.ServerRespose;
import cn.wisdsoft.mmall.pojo.MmallCategory;
import cn.wisdsoft.mmall.pojo.MmallProduct;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    ServerRespose insertCategory(String Categoryname, Integer parentId);

    ServerRespose updateCategory(String CategoryName,Integer CategoryId);

    ServerRespose<List<MmallCategory>> selectchildparallelCategory(Integer CategoryId);

    ServerRespose<List<Integer>> selectChildCategory(Integer CategoryId);
}

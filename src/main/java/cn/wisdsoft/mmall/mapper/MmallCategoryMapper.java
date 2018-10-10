package cn.wisdsoft.mmall.mapper;

import cn.wisdsoft.mmall.pojo.MmallCategory;
import cn.wisdsoft.mmall.pojo.MmallCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MmallCategoryMapper {
    int countByExample(MmallCategoryExample example);

    int deleteByExample(MmallCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MmallCategory record);

    int insertSelective(MmallCategory record);

    List<MmallCategory> selectByExample(MmallCategoryExample example);

    MmallCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MmallCategory record, @Param("example") MmallCategoryExample example);

    int updateByExample(@Param("record") MmallCategory record, @Param("example") MmallCategoryExample example);

    int updateByPrimaryKeySelective(MmallCategory record);

    int updateByPrimaryKey(MmallCategory record);
}
package cn.wisdsoft.mmall.mapper;

import cn.wisdsoft.mmall.pojo.MmallProduct;
import cn.wisdsoft.mmall.pojo.MmallProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MmallProductMapper {
    int countByExample(MmallProductExample example);

    int deleteByExample(MmallProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MmallProduct record);

    int insertSelective(MmallProduct record);

    List<MmallProduct> selectByExample(MmallProductExample example);

    MmallProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MmallProduct record, @Param("example") MmallProductExample example);

    int updateByExample(@Param("record") MmallProduct record, @Param("example") MmallProductExample example);

    int updateByPrimaryKeySelective(MmallProduct record);

    int updateByPrimaryKey(MmallProduct record);
}
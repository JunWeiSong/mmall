package cn.wisdsoft.mmall.mapper;

import cn.wisdsoft.mmall.pojo.MmallPayInfo;
import cn.wisdsoft.mmall.pojo.MmallPayInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MmallPayInfoMapper {
    int countByExample(MmallPayInfoExample example);

    int deleteByExample(MmallPayInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MmallPayInfo record);

    int insertSelective(MmallPayInfo record);

    List<MmallPayInfo> selectByExample(MmallPayInfoExample example);

    MmallPayInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MmallPayInfo record, @Param("example") MmallPayInfoExample example);

    int updateByExample(@Param("record") MmallPayInfo record, @Param("example") MmallPayInfoExample example);

    int updateByPrimaryKeySelective(MmallPayInfo record);

    int updateByPrimaryKey(MmallPayInfo record);
}
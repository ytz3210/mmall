package cn.yang.tmall.dao;

import cn.yang.tmall.pojo.Dict;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Yangtz
 * @ClassName: DictMapper
 * @Description:
 * @create 2020-02-28 22:35
 */
@Mapper
public interface DictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dict record);

    int insertSelective(Dict record);

    Dict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dict record);

    int updateByPrimaryKey(Dict record);
}

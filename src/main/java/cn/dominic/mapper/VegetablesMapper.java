package cn.dominic.mapper;

import cn.dominic.pojo.Vegetables;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 22:23
 * @Modify:
 **/
@Mapper
@Repository
public interface VegetablesMapper {
    void insert(Vegetables vegetables);

    void update(Vegetables vegetables);

    List<Vegetables> select(String name);

    Vegetables selectById(Integer id);

    void delete(Integer id);
}

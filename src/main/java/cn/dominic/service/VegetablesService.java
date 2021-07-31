package cn.dominic.service;

import cn.dominic.pojo.Vegetables;

import java.util.List;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 22:42
 * @Modify:
 **/
public interface VegetablesService {
    void insert(Vegetables vegetables);

    void update(Vegetables vegetables);

    List<Vegetables> select(String name);

    Vegetables selectById(Integer id);

    void delete(Integer id);
}

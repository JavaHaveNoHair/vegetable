package cn.dominic.Impl;

import cn.dominic.mapper.VegetablesMapper;
import cn.dominic.pojo.Vegetables;
import cn.dominic.service.VegetablesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 22:43
 * @Modify:
 **/
@Service("vegetablesService")
public class VegetablesServiceImpl implements VegetablesService {

    @Resource
    private VegetablesMapper vegetablesMapper;

    @Override
    public void insert(Vegetables vegetables) {
        vegetablesMapper.insert(vegetables);
    }

    @Override
    public void update(Vegetables vegetables) {
        vegetablesMapper.update(vegetables);
    }

    @Override
    public List<Vegetables> select(String name) {
        return vegetablesMapper.select(name);
    }

    @Override
    public Vegetables selectById(Integer id) {
        return vegetablesMapper.selectById(id);
    }

    @Override
    public void delete(Integer id) {
        vegetablesMapper.delete(id);
    }
}

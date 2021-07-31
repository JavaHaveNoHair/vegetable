package cn.dominic.util;

import cn.dominic.pojo.Config;
import org.springframework.beans.BeanUtils;

/**
 * @Description :全局对象
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 22:42
 * @Modify:
 **/
public class GlobalVariable {


    /**
     * 全局配置对象
     */
    public static volatile Config globalConfig = new Config();

    private GlobalVariable() {
    }

    public static synchronized Config changeGlobalConfig(Config config) {
        BeanUtils.copyProperties(config, globalConfig);
        return globalConfig;
    }

    public static Config getGlobalConfig() {
        return globalConfig;
    }
}

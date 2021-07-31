package cn.dominic.util;

import cn.dominic.pojo.Config;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 22:42
 * @Modify:
 **/
public class DataConfigUtil {

    public static final Map<String, Config> map = Maps.newConcurrentMap();

    public static void putMap(String key, Config config) {
        map.put(key, config);
        System.out.println(map.get(key));
    }

    public static Config getConfig(String key) {
        return map.getOrDefault(key, null);
    }
}

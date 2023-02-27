package com.shenyy.pretendto.utils;

import io.micrometer.core.instrument.util.StringUtils;

import java.util.Map;

/**
 * 通用工具类
 *
 * @author shenyy
 */
public class CommonUtils {
    /**
     * 常量定义
     */
    public static Integer FILENAMEMAXLENGTH = 128;


    /**
     * 判断键值对是否存在且不为空
     */
    public static boolean isKeyValueNotEmpty(Map object, String key) {
        return object.keySet().contains(key) && (object.get(key) != null) && StringUtils.isNotEmpty(object.get(key).toString());
    }
}

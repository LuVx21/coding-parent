package org.luvx.coding.common.util;

import com.alibaba.fastjson2.JSON;

public class ToString {
    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof CharSequence || obj instanceof Number) {
            return obj.toString();
        }
        Class<?> cl = obj.getClass();
        if (cl.isArray()) {
            // return ArrayUtils.toString(obj);
            return JSON.toJSONString(obj);
        }
        return obj.toString();
    }
}

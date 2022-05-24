package org.luvx.common.util;

import java.util.Arrays;

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
            return Arrays.toString((Object[]) obj);
        }
        return obj.toString();
    }
}

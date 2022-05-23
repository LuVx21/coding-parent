package org.luvx.common.util;

import java.util.Arrays;

public class ToString {
    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        Class<?> cl = obj.getClass();
        if (cl == String.class) {
            return (String) obj;
        } else if (cl.isArray()) {
            return Arrays.toString((Object[]) obj);
        }
        return obj.toString();
    }
}
